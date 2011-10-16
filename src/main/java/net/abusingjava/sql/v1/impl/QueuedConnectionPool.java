package net.abusingjava.sql.v1.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.WrappedException;
import net.abusingjava.sql.v1.ConnectionProvider;
import net.abusingjava.sql.v1.Credentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-16")
public class QueuedConnectionPool implements ConnectionProvider {

	private final Logger $logger = LoggerFactory.getLogger(getClass());
	
	private Credentials $credentials = null;
	private int $poolSize = 4;
	private final ConnectionProviderState $state = ConnectionProviderState.INITIALIZED;
	private List<ConnectionInfo> $connections = new ArrayList<ConnectionInfo>($poolSize);
	private final BlockingQueue<Runnable> $taskQueue = new LinkedBlockingQueue<Runnable>();
	private final Thread $worker = new Thread(new Worker($taskQueue));
	
	private class ConnectionInfo {
		
		private boolean $inUse;
		private final Connection $connection;

		public ConnectionInfo(final Connection $connection) {
			this.$connection = $connection;
		}
	}
	
	private class Worker implements Runnable {

		private final BlockingQueue<Runnable> $tasks;
		
		Worker(final BlockingQueue<Runnable> $tasks) {
			this.$tasks = $tasks;
		}
		
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					$tasks.take().run();
				} catch (InterruptedException $exc) {
					
				} catch (RuntimeException $exc) {
					$logger.error($exc.getMessage(), $exc);
				}
			}
		}
		
	}
	
	private class CreateConnection implements Runnable {

		@Override
		public void run() {
			try {
			Connection $connection = DriverManager.getConnection(
					$credentials.getJdbcUrl(),
					$credentials.getUsername(),
					$credentials.getPassword());
			$connections.add(new ConnectionInfo($connection));
			} catch (SQLException $exc) {
				throw new WrappedException($exc);
			}
		}
	}
	
	public QueuedConnectionPool() {
		
	}
	
	@Override
	public void setCredentials(final Credentials $credentials) {
		this.$credentials = $credentials;
	}
	
	public void setPoolSize(final int $poolSize) {
		this.$poolSize = $poolSize;
		if ($state == ConnectionProviderState.INITIALIZED) {
			$connections = new ArrayList<ConnectionInfo>(this.$poolSize);
		} else if ($state == ConnectionProviderState.READY) {
			if ($poolSize > $connections.size()) {
				for (int $i = 0; $i < ($poolSize - $connections.size()); $i++) {
					$taskQueue.add(new CreateConnection());
				}
			} else {
				
			}
		}
	}
	
	public int getPoolSize() {
		return $poolSize;
	}

	@Override
	public void open() {
		
	}

	@Override
	public Connection getConnection() {
		return null;
	}

	@Override
	public void releaseConnection(final Connection $connection) {
		
	}

	@Override
	public void close() {
		if ($state == ConnectionProviderState.CLOSED) {
			throw new IllegalStateException("Can not close ConnectionPool: Already closed.");
		}
		if ($state == ConnectionProviderState.INITIALIZED) {
			throw new IllegalStateException("Can not close ConnectionPool: Not opened.");
		}
	}

	@Override
	public ConnectionProviderState getState() {
		return $state;
	}
}
