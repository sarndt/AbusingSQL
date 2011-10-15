package net.abusingjava.sql.v1.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.sql.v1.ConnectionProvider;
import net.abusingjava.sql.v1.DatabaseException;
import net.abusingjava.sql.v1.Transaction;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public class GenericTransaction implements Transaction {

	private Connection $connection;
	private ConnectionProvider $connectionProvider;
	private TransactionState $state = TransactionState.UNCOMMITED;
	
	public GenericTransaction(final ConnectionProvider $connectionProvider) {
		this.$connectionProvider = $connectionProvider;
		this.$connection = $connectionProvider.getConnection();
		try {
			$connection.setAutoCommit(false);
		} catch (SQLException $exc) {
			$state = TransactionState.ERRORNEOUS;
			throw new DatabaseException("Could not begin Transaction (Connection.setAutoCommit(false) failed).", $exc);
		}
	}

	@Override
	public void commit() {
		if ($state != TransactionState.UNCOMMITED) {
			throw new DatabaseException("Can not commit Transaction if state is not UNCOMMITED.", new IllegalStateException());
		}
		try {
			$connection.commit();
			$connection.setAutoCommit(true);
			$state = TransactionState.COMMITED;
			$connectionProvider.releaseConnection($connection);
		} catch (SQLException $exc) {
			$state = TransactionState.ERRORNEOUS;
			throw new DatabaseException("Could not commit Transaction (Connection.commit() failed)", $exc);
		} finally {
			cleanUp();
		}
	}

	@Override
	public void rollback() {
		if ($state != TransactionState.UNCOMMITED) {
			throw new DatabaseException("Can not rollback Transaction if state is not UNCOMMITED.", new IllegalStateException());
		}
		try {
			$connection.rollback();
		} catch (SQLException $exc) {
			$state = TransactionState.ERRORNEOUS;
			throw new DatabaseException("Could not rollback Transaction (Connection.rollback() failed)", $exc);
		} finally {
			cleanUp();
		}
	}

	@Override
	public TransactionState getState() {
		return $state;
	}
	
	@Override
	public Connection getConnection() {
		return $connection;
	}
	
	private void cleanUp() {
		$connection = null;
		$connectionProvider = null;
	}
	
}
