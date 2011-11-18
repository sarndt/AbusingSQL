package net.abusingjava.sql.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import net.abusingjava.AbusingJava;
import net.abusingjava.Author;
import net.abusingjava.Tuple;
import net.abusingjava.sql.ConnectionProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple ConnectionPool that caches a certain amount of connections always.
 */
@Author("Julian Fleischer")
public class ConnectionPool2 implements ConnectionProvider {

	final private ConcurrentHashMap<Long, Tuple<Connection, Semaphore>> $connections;

	@SuppressWarnings("unused")
	final private int $poolsize;
	final private int $loginTimeout;

	final private String $url;
	final private String $user;
	final private String $password;

	@SuppressWarnings("unused")
	final private Logger $logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getSchemaNameFromURL() {
		return $url.substring($url.lastIndexOf('/') + 1);
	}

	public ConnectionPool2(
			final String $driverClassName,
			final String $url,
			final String $user,
			final String $password,
			final int $poolsize,
			final int $loginTimeout,
			@SuppressWarnings("unused") final int $reaperDelay,
			@SuppressWarnings("unused") final int $reaperTimeout,
			@SuppressWarnings("unused") final int $connectionTimeout)
			throws ClassNotFoundException {
		this.$url = $url;
		this.$user = $user;
		this.$password = $password;
		this.$poolsize = $poolsize;
		this.$loginTimeout = $loginTimeout;

		Class.forName($driverClassName);
		DriverManager.setLoginTimeout(this.$loginTimeout);

		$connections = new ConcurrentHashMap<Long, Tuple<Connection, Semaphore>>($poolsize);
	}

	@Override
	public Connection getConnection() throws SQLException {
		Long $threadId = new Long(Thread.currentThread().getId());

		if ($connections.containsKey($threadId)) {
			Tuple<Connection, Semaphore> $t = $connections.get($threadId);
			try {
				$t.snd().acquire();
				return $t.fst();
			} catch (InterruptedException $exc) {
				throw new RuntimeException($exc);
			}
		}
		Connection $c = DriverManager.getConnection($url, $user, $password);
		Semaphore $s = new Semaphore(0);
		$connections.put($threadId, AbusingJava.t($c, $s));
		return $c;
	}

	@Override
	public boolean release(final Connection $connection) {
		Long $threadId = new Long(Thread.currentThread().getId());

		$connections.get($threadId).snd().release();

		return true;
	}

	@Override
	public void close() {
		for (Entry<Long, Tuple<Connection, Semaphore>> $entry : $connections.entrySet()) {
			try {
				$entry.getValue().fst().close();
			} catch (SQLException $exc) {

			}
		}
	}
}
