package net.abusingjava.sql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

import net.abusingjava.Author;
import net.abusingjava.sql.ConnectionProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple ConnectionPool that caches a certain amount of connections always.
 */
@Author("Julian Fleischer")
public class ConnectionPool implements ConnectionProvider {

	final private Connection[] $connections;
	final private boolean[] $connections_available;
	final private int $poolsize;
	final private int $loginTimeout;

	final private String $url;
	final private String $user;
	final private String $password;
	
	final private Semaphore $barrier;
	final private Semaphore $mutex;
	
	final private Logger $logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getSchemaNameFromURL() {
		return $url.substring($url.lastIndexOf('/') + 1);
	}

	public ConnectionPool(
			final String $driverClassName,
			final String $url,
			final String $user,
			final String $password,
			final int $poolsize,
			final int $loginTimeout,
			@SuppressWarnings("unused") final int $reaperDelay,
			@SuppressWarnings("unused") final int $reaperTimeout,
			@SuppressWarnings("unused") final int $connectionTimeout)
			throws ClassNotFoundException, SQLException {
		this.$url = $url;
		this.$user = $user;
		this.$password = $password;
		this.$poolsize = $poolsize;
		this.$loginTimeout = $loginTimeout;

		Class.forName($driverClassName);
		DriverManager.setLoginTimeout(this.$loginTimeout);
		
		$barrier = new Semaphore(0, true);
		$mutex = new Semaphore(0, true);
		$connections = new Connection[this.$poolsize];
		$connections_available = new boolean[this.$poolsize];
		
		for (int $i = 0; $i < this.$poolsize; $i++) {
			$connections[$i] = DriverManager.getConnection($url, $user, $password);
			$connections[$i].setAutoCommit(true);
			$connections_available[$i] = true;
		}
		
		$barrier.release(this.$poolsize);
		$mutex.release();
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			$barrier.acquire();
			$mutex.acquire();
			for (int $i = 0; $i < $poolsize; $i++) {
				if ($connections_available[$i]) {
					$connections_available[$i] = false;
					
					if ($connections[$i].isClosed()) {
						try {
							$connections[$i] = DriverManager.getConnection($url, $user, $password);
						} catch (Exception $exc) {
							$connections_available[$i] = true;
							$barrier.release();
						}
					}
					
					return $connections[$i];
				}
			}
		} catch (InterruptedException $exc) {
			throw new RuntimeException($exc);
		} finally {
			$mutex.release();
		}
		throw new RuntimeException("Could not obtain connection.");
	}

	@Override
	public boolean release(final Connection $connection) {
		try {
			$mutex.acquire();
			for (int $i = 0; $i < $poolsize; $i++) {
				if ($connections[$i] == $connection) {
					$connections_available[$i] = true;
				}
			}
		} catch (InterruptedException $exc) {
			throw new RuntimeException($exc);
		} finally {
			$mutex.release();
		}
		$barrier.release();
		return true;
	}

	@Override
	public void close() {
		for (Connection $c : $connections) {
			try {
				$c.close();
			} catch (SQLException $exc) {
				$logger.debug("Finally closing the connection failed.", $exc);
			}
		}
	}
}
