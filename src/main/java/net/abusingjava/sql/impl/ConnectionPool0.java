package net.abusingjava.sql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.sql.ConnectionProvider;
import net.abusingjava.sql.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple ConnectionPool that caches a certain amount of connections always.
 */
@Author("Julian Fleischer")
@Since("2011-08-15")
@Version("2011-10-16")
public class ConnectionPool0 implements ConnectionProvider {

	final ArrayList<ConnectionObject> $connections;
	final int $poolsize;
	final int $loginTimeout;

	final String $url;
	final String $user;
	final String $password;

	final int $reaperDelay;
	final int $reaperTimeout;
	final int $connectionTimeout;

	final Logger $logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getSchemaNameFromURL() {
		return $url.substring($url.lastIndexOf('/') + 1);
	}

	class ConnectionReaper implements Runnable {

		@Override
		public void run() {
			for (;;) {
				try {
					Thread.sleep($reaperDelay);
				} catch (InterruptedException $exc) {
				}
				if (Thread.interrupted()) {
					break;
				}
				reapConnections();
			}
		}

	}

	class ConnectionObject {

		final Connection $connection;
		long $timestamp;
		boolean $inUse = false;

		ConnectionObject(@SuppressWarnings("unused") final int $timeout) throws SQLException {
			$connection = DriverManager.getConnection($url, $user, $password);
		}

		boolean lease() {
			synchronized ($connection) {
				if ($inUse) {
					return false;
				}
				$inUse = true;
				$timestamp = System.currentTimeMillis();
				return true;
			}
		}

		boolean validate() {
			try {
				synchronized ($connection) {
					return $connection.isValid($connectionTimeout);
				}
			} catch (SQLException $exc) {
				return false;
			}
		}

	}

	/**
	 * Standard-Konstruktor der alle maßgeblichen Konfigurationsoptionen
	 * übergeben kriegt.
	 * 
	 * @param $driverClassName
	 *            Der Name des zu ladenden Datenbank-Treibers.
	 * @param $url
	 *            Der JDBC-Url zur Datenbank (bspw. jdbc:mysql://...).
	 * @param $user
	 *            Der für die Datenbank zu benutzende Nutzername.
	 * @param $password
	 *            Das Passwort zur Datenbank.
	 * @param $poolsize
	 *            Die Anzahl der in Vorbereitung zu haltenden Verbindungen.
	 * @param $loginTimeout
	 *            The number of seconds until a connection attempt should fail.
	 * @param $reaperDelay
	 *            Das Interval (in Millisekunden) in dem der Connection-Reaper
	 *            alte Verbindungen entsorgt.
	 * @param $reaperTimeout
	 *            Die Anzahl Millisekunden die eine Verbindung nicht mehr
	 *            genutzt sein muss, damit der Reaper sie entsorgt.
	 * @param $connectionTimeout
	 *            Die Anzahl der Millisekunden die eine Verbindung Zeit hat
	 *            einen erfolgreichen ping zu senden, bevor sie als invalid
	 *            angesehen wird und erneuert würde.
	 * 
	 * @throws ClassNotFoundException
	 *             Wenn die durch $driverClassName angegebene Klasse nicht
	 *             gefunden wurde.
	 * @throws SQLException
	 *             Wenn es Fehler beim Erstellen der Verbindungen gab.
	 */
	public ConnectionPool0(final String $driverClassName, final String $url, final String $user, final String $password,
			final int $poolsize, final int $loginTimeout, final int $reaperDelay, final int $reaperTimeout,
			final int $connectionTimeout)
			throws ClassNotFoundException, SQLException {
		this.$url = $url;
		this.$user = $user;
		this.$password = $password;
		this.$poolsize = $poolsize;
		this.$loginTimeout = $loginTimeout;
		this.$reaperDelay = $reaperDelay;
		this.$connectionTimeout = $connectionTimeout;
		this.$reaperTimeout = $reaperTimeout;

		$connections = new ArrayList<ConnectionObject>($poolsize);
		Class.forName($driverClassName);

		for (int $i = 0; $i < $poolsize; $i++) {
			$connections.add(new ConnectionObject($loginTimeout));
		}

		Thread $reaper = new Thread(new ConnectionReaper());
		$reaper.setDaemon(true);
		$reaper.start();
	}

	void reapConnections() {
		synchronized ($connections) {
			long $stale = System.currentTimeMillis() - $reaperTimeout;
			Set<ConnectionObject> $remove = new HashSet<ConnectionObject>();
			for (ConnectionObject $o : $connections) {
				if ($o.$inUse) {
					if (($o.$timestamp < $stale) && !$o.validate()) {
						$remove.add($o);
					}
				} else if (!$o.validate()) {
					$remove.add($o);
				}
			}
			for (ConnectionObject $r : $remove) {
				$remove.remove($r);
			}
			for (int $i = 0; $i < ($poolsize - $connections.size()); $i++) {
				try {
					$connections.add(new ConnectionObject($loginTimeout));
				} catch (SQLException $exc) {
				}
			}
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			synchronized ($connections) {
				for (ConnectionObject $o : $connections) {
					if ($o.lease()) {
						return $o.$connection;
					}
				}
				ConnectionObject $o = new ConnectionObject($loginTimeout);
				$connections.add($o);
				return $o.$connection;
			}
		} finally {
			$logger.debug("Open connections: {}", $connections.size());
		}
	}

	@Override
	public boolean release(final Connection $connection) {
		if ($connection == null) {
			return true;
		}
		try {
			if (!$connection.getAutoCommit()) {
				try {
					$connection.commit();
				} catch (SQLException $exc) {
					throw new DatabaseException($exc);
				}
				$connection.setAutoCommit(true);
			}
		} catch (SQLException $exc) {
			$logger.error("Error committing uncommitted connection while releasing.", $exc);
		}
		synchronized ($connections) {
			for (ConnectionObject $o : $connections) {
				if ($o.$connection == $connection) {
					$o.$inUse = false;
					if ($connections.size() > $poolsize) {
						try {
							$o.$connection.close();
							$connections.remove($o);
						} catch (SQLException $exc) {
							$logger.error("Could not close connection while releasing", $exc);
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void close() {
		synchronized ($connections) {
			for (ConnectionObject $o : $connections) {
				try {
					if (!$o.$connection.getAutoCommit()) {
						$o.$connection.commit();
					}
				} catch (SQLException $exc) {
					$logger.error("Error committing uncommitted connection while closing connection pool.", $exc);
				}
				try {
					$o.$connection.close();
				} catch (SQLException $exc) {
					$logger.error("Could not close connection while closing connection pool.", $exc);
				}
			}
		}
	}
}
