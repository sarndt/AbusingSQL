package net.abusingjava.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

	/**
	 * Gibt eine Verbindung aus dem ConnectionPool zurück beziehungsweise erstellt eine (wenn alle Verbindungen bereits vergeben sind).
	 * Diese Methode ist thread-safe.
	 */
	Connection getConnection() throws SQLException;

	/**
	 * Gibt einen Verbindung an den ConnectionPool zurück. Diese Methode ist thread-safe.
	 */
	boolean release(final Connection $connection);

	/**
	 * Shutdown this connection pool.
	 */
	void close();
}