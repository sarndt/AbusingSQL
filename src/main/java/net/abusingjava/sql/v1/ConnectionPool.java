package net.abusingjava.sql.v1;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.event.OffersPropertyChangeEvents;
import net.abusingjava.event.OffersStateChangeEvents;
import net.abusingjava.event.StateChangeEvent;
import net.abusingjava.event.StateChangeListener;
import net.abusingjava.sql.v1.ConnectionProvider.ConnectionProviderState;
import net.abusingjava.sql.v1.impl.WrappedConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ConnectionPool that uses Semaphores to synchronize acccess.
 */
@Author("Julian Fleischer")
@Version("2011-10-17")
@Since(version = "1.0", value = "2011-10-17")
public class ConnectionPool implements
		ConnectionProvider,
		OffersPropertyChangeEvents,
		OffersStateChangeEvents<ConnectionProviderState> {

	final Logger $logger = LoggerFactory.getLogger(getClass());

	final Semaphore $queueMaster = new Semaphore(0, true);
	final Semaphore $connectionMaster = new Semaphore(0, true);
	List<ConnectionHolder> $connections;

	private ConnectionProviderState $state = ConnectionProviderState.INITIALIZED;
	private Credentials $credentials = null;
	private int $poolSize = 4;
	private final int $reaperDelay = 5000;
	private final int $validationTimeout = 1000;
	private final int $maxAge = 3500;

	private final PropertyChangeSupport $propertyChangeSupport = new PropertyChangeSupport(this);
	private final List<StateChangeListener<ConnectionProviderState>> $stateChangeListeners = new LinkedList<StateChangeListener<ConnectionProviderState>>();

	/**
	 * Holds Information about a Connection *and* the connection.
	 */
	class ConnectionHolder {
		final WrappedConnection $connection;
		final long $created;
		long $lastValidated;
		boolean $inUse;

		ConnectionHolder(final Connection $connection) {
			this.$connection = new WrappedConnection($connection);
			$created = System.currentTimeMillis();
			$lastValidated = $created;
			$inUse = false;
		}

		boolean isInUse() {
			return $inUse;
		}

		void setInUse(final boolean $inUse) {
			this.$inUse = $inUse;
		}

		Connection getConnection() {
			return $connection;
		}

		long getMostRecentlyUsedTime() {
			return Math.max($lastValidated, $connection.getMostRecentUseTime());
		}

		boolean validate() {
			$lastValidated = System.currentTimeMillis();
			try {
				if ($connection.isValid($validationTimeout)) {
					$lastValidated = System.currentTimeMillis();
					return true;
				}
			} catch (SQLException $exc) {

			}
			return false;
		}

		public void closeConnection() {
			try {
				$connection.close();
			} catch (SQLException $exc) {
				$logger.error("Error closing connection.", $exc);
			}
		}
	}

	/**
	 * The Reaper that checks if all pooled connections are still valid and
	 * occasionally reconnects.
	 */
	class ConnectionReaper implements Runnable {
		@Override
		public void run() {
			for (;;) {
				try {
					Thread.sleep($reaperDelay);
					long $expired = System.currentTimeMillis() - $maxAge;
					Set<ConnectionHolder> $holdersToRemove = new HashSet<ConnectionHolder>();
					$queueMaster.acquire();
					try {
						for (ConnectionHolder $holder : $connections) {
							if ($holder.isInUse()) {
								if ($holder.getMostRecentlyUsedTime() < $expired) {
									$holder.setInUse(false);
								}
							}
							if (!$holder.isInUse()) {
								if (!$holder.validate()) {
									$holdersToRemove.add($holder);
								}
							}
						}
						for (ConnectionHolder $holder : $holdersToRemove) {
							$connections.remove($holder);
							$connectionMaster.release();
						}
					} finally {
						$queueMaster.release();
					}
					refreshPool();
				} catch (InterruptedException $exc) {
					break;
				}
			}
		}
	}

	/**
	 * Default constructor ($poolSize = 4). Note that you still have to set
	 * Credentials for connecting.
	 * 
	 * @see #setCredentials(Credentials)
	 * @see #setPoolSize(int)
	 */
	public ConnectionPool() {
		$connections = new ArrayList<ConnectionHolder>($poolSize);
	}

	/**
	 * Constructs a ConnectionPool with the given $poolSize. Note that you still
	 * have to set Credentials for connecting.
	 * 
	 * @see #setCredentials(Credentials)
	 */
	public ConnectionPool(final int $poolSize) {
		this.$poolSize = $poolSize;
		$connections = new ArrayList<ConnectionHolder>($poolSize);
	}

	/**
	 * Constructs a ConnectionPool with the given $poolSize and $credentials for
	 * connecting.
	 */
	public ConnectionPool(final Credentials $credentials, final int $poolSize) {
		this.$poolSize = $poolSize;
		this.$credentials = $credentials;
		$connections = new ArrayList<ConnectionHolder>($poolSize);
	}

	@Override
	public void setCredentials(final Credentials $credentials) {
		this.$credentials = $credentials;
	}

	public int getPoolSize() {
		return $poolSize;
	}

	public void setPoolSize(final int $poolSize) {
		if ($state != ConnectionProviderState.INITIALIZED) {
			throw new IllegalStateException("You have to set the $poolSize *before* opening the ConnectionPool.");
		}
		int $oldPoolSize = this.$poolSize;
		this.$poolSize = $poolSize;
		this.$connections = new ArrayList<ConnectionHolder>($poolSize);
		$propertyChangeSupport.firePropertyChange("poolSize", $oldPoolSize, $poolSize);
	}

	private void createConnection() {
		try {
			// create the connection using the given $credentials.
			Connection $connection = DriverManager.getConnection(
					$credentials.getJdbcUrl(),
					$credentials.getUsername(),
					$credentials.getPassword()
					);
			// add the connection to our pool.
			$connections.add(new ConnectionHolder($connection));
			// increase semaphore, since there are more resources now.
			$connectionMaster.release();
		} catch (SQLException $exc) {

		}
	}

	@Override
	public void open() {
		// Check if not alreay opened...
		if ($state == ConnectionProviderState.READY) {
			throw new IllegalStateException("ConnectionPool is open already.");
		}
		// ...and not garbage yet.
		if ($state == ConnectionProviderState.CLOSED) {
			throw new IllegalStateException("ConnectionPool is closed, please create a new one.");
		}
		// check if credentials have been set
		if ($credentials == null) {
			throw new IllegalStateException("No credentials have been set.");
		}
		// create connections
		refreshPool();
		// start the connection reaper
		new Thread(new ConnectionReaper()).start();
		$queueMaster.release();
		changeState(ConnectionProviderState.READY);
	}

	void refreshPool() {
		try {
			$queueMaster.acquire();
			try {
				int $connectionsToCreate = $poolSize - $connections.size();
				for (int $i = 0; $i < $connectionsToCreate; $i++) {
					createConnection();
				}
			} finally {
				$queueMaster.release();
			}
		} catch (InterruptedException $exc) {
			$logger.warn("Could not refresh pool, Thread was interrupted.", $exc);
		}
	}

	@Override
	public Connection getConnection() {
		if ($state != ConnectionProviderState.READY) {
			throw new IllegalStateException("");
		}
		try {
			// make sure there are connections available
			$connectionMaster.acquire();
			// make sure we’re the only one accessing the connections pool
			$queueMaster.acquire();
			try {

			} finally {
				$queueMaster.release();
			}
		} catch (InterruptedException $exc) {
			$logger.warn("Interrupted while trying to acquire semaphore.", $exc);
		} catch (Exception $exc) {
			$connectionMaster.release();
		}
		return null;
	}

	@Override
	public void releaseConnection(final Connection $connection) {
		try {
			$queueMaster.acquire();
			try {
				ConnectionHolder $connectionHolder = null;
				for (ConnectionHolder $holder : $connections) {
					if ($holder.getConnection() == $connection) {
						$connectionHolder = $holder;
						break;
					}
				}
				if ($connectionHolder == null) {
					try {
						$connection.close();
					} catch (SQLException $exc) {
						$logger.warn("Could not close unregistered connection.", $exc);
					}
					$logger.warn(
							"Closing unregistered connection.",
							new NullPointerException(
									"$connectionHolder is null, thus the requested Connection has not been found in the internal registry."));
					return;
				}
				try {
					$connectionHolder.closeConnection();
				} finally {
					$connections.remove($connectionHolder);
				}
			} finally {
				$queueMaster.release();
				$connectionMaster.release();
			}
		} catch (InterruptedException $exc) {
			$logger.warn("Interrupted while trying to acquire $queueMaster.", $exc);
		}
	}

	@Override
	public void close() {
		if ($state == ConnectionProviderState.CLOSED) {
			throw new IllegalStateException("Can not close ConnectionPool: Already closed.");
		}
		if ($state == ConnectionProviderState.INITIALIZED) {
			throw new IllegalStateException("Can not close ConnectionPool: Not opened.");
		}
		try {
			$queueMaster.acquire();
			for (ConnectionHolder $info : $connections) {
				$info.closeConnection();
			}
		} catch (InterruptedException $exc) {

		}
	}

	@Override
	public ConnectionProviderState getState() {
		return $state;
	}

	private void changeState(final ConnectionProviderState $state) {
		ConnectionProviderState $oldState = this.$state;
		this.$state = $state;
		for (StateChangeListener<ConnectionProviderState> $stateChangeListener : $stateChangeListeners) {
			try {
				$stateChangeListener
						.stateChanged(new StateChangeEvent<ConnectionProviderState>(this, $oldState, $state));
			} catch (Exception $exc) {
				$logger.warn("Error invoking StateChangeListener.", $exc);
			}
		}
	}

	@Override
	public void addStateChangeListener(final StateChangeListener<ConnectionProviderState> $listener) {
		$stateChangeListeners.add($listener);
	}

	@Override
	public void removeStateChangeListener(final StateChangeListener<ConnectionProviderState> $listener) {
		$stateChangeListeners.remove($listener);
	}

	@Override
	@SuppressWarnings("unchecked")
	public StateChangeListener<ConnectionProviderState>[] getStateChangeListeners() {
		return $stateChangeListeners
				.toArray((StateChangeListener<ConnectionProviderState>[]) new StateChangeListener<?>[$stateChangeListeners
						.size()]);
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener $listener) {
		$propertyChangeSupport.addPropertyChangeListener($listener);
	}

	@Override
	public void removePropertyChangeListener(final PropertyChangeListener $listener) {
		$propertyChangeSupport.removePropertyChangeListener($listener);
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return $propertyChangeSupport.getPropertyChangeListeners();
	}
}
