package net.abusingjava.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
public interface DatabaseExtravaganza {

	/**
	 * Escapes a name according to the conventions of a specific database (like `MySQL` or "Postgres").
	 */
	String escapeName(final String $name);
	
	/**
	 * 
	 */
	Object get(ResultSet $resultSet, int $index);
	
	Object get(ResultSet $resultSet, String $name);
	
	void set(PreparedStatement $stmt, int $index, Object $value);

	String getSqlType(Property $property);
	
	/**
	 * Returns the canonical name of the database driver being used.
	 */
	String getDriverName();
	
	/**
	 * Creates a database using the given $pool and $schema.
	 */
	void createDatabase(ConnectionPool $pool, Schema $schema);

	/**
	 * Drops an existing database using the given $pool and $schema.
	 */
	void dropDatabase(ConnectionPool $pool, Schema $schema);

	/**
	 * 
	 */
	String getDeleteQuery(Interface $interface, int $id);

}
