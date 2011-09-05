package net.abusingjava.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.schema.Property;
import net.abusingjava.sql.schema.Schema;

@Author("Julian Fleischer")
@Version("2011-08-15")
public interface DatabaseExtravaganza {

	/**
	 * Escapes a name according to the conventions of a specific database (like
	 * `MySQL` or "Postgres").
	 */
	String escapeName(final String $name);

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
	void createDatabase(ConnectionProvider $pool, Schema $schema);

	/**
	 * Drops an existing database using the given $pool and $schema. <br>
	 * <br>
	 * Note: Tables NOT belonging to the current Schema defined by this
	 * DatabaseAccess are completely ignored. This means if the Database Access
	 * used to define the Database had MORE tables than the current
	 * DatabaseAccess used to access the database now, the tables not incl uded
	 * in the current DatabaseAccess will be ignored. <br>
	 * <br>
	 * If you wish to drop everything use {@link #dropAllTablesInDatabase(ConnectionProvider) dropAllTablesInDatabase}.
	 */
	void dropDatabase(ConnectionProvider $pool, Schema $schema);

	/**
	 * Drops the current DB and recreates it without tables afterwards. This is
	 * useful if you need to drop all tables in the DB, regardless of which
	 * Schema was used to generate your DatabaseAccess object. <br>
	 * <br>
	 * See also the JavaDoc for the method {@link #dropDatabase(ConnectionProvider, Schema)
	 * dropDatabase()}. <br>
	 * <br>
	 * Note: User must have DROP-Privilege or be the owner of the Database!
	 */
	void dropAllTablesInDatabase(ConnectionProvider $pool);

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
	String makeSelectQuery(String $table, Integer $offset, Integer $limit);

	/**
	 * 
	 */
	String makeSelectQuery(String $table, int $id);

	/**
	 * 
	 */
	Object get(ResultSet $resultSet, String $column, Class<?> $javaType) throws SQLException;

	/**
	 * 
	 */
	Object get(ResultSet $resultSet, int $i, int columnType);

}
