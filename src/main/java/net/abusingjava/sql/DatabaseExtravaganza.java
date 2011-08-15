package net.abusingjava.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	/**
	 * 
	 */
	Object get(ResultSet $resultSet, String $name);
	
	/**
	 * 
	 */
	void set(PreparedStatement $stmt, int $index, Object $value);

	/**
	 * 
	 */
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
	void doDelete(Connection $c, String $table, int $id) throws SQLException;

	/**
	 * 
	 */
	int doInsert(Connection $c, String $table, String[] $properties, Object[] $values) throws SQLException;

	/**
	 * 
	 */
	void doUpdate(Connection $c, String $table, String[] $properties, Object[] $values, int $id) throws SQLException;

	/**
	 * 
	 */
	Object get(ResultSet $resultSet, String $column, Class<?> $javaType) throws SQLException;

}
