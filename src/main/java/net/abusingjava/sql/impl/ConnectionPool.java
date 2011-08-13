package net.abusingjava.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

	/**
	 * Gibt eine Verbindung aus dem ConnectionPool zurück beziehungsweise erstellt eine (wenn alle Verbindungen bereits vergeben sind).
	 * Diese Methode ist thread-safe.
	 */
	Connection getConnection() throws SQLException;

	/**
	 * Gibt einen Verbindung an den ConnectionPool zurück. Diese Methode ist thread-safe.
	 */
	boolean release(final Connection $connection);

}