package net.abusingjava.sql;

import java.sql.Connection;

import net.abusingjava.Author;
import net.abusingjava.Version;

/**
 * 
 */
@Author("Julian Fleischer")
@Version("2011-08-15")
public interface DatabaseAccess {

	/**
	 * Retrieves a Connection from the underlying ConnectionPool (or other kind of Connection-Provider, depending on the Implementation).
	 */
	Connection getConnection();

	/**
	 * Gives a Connection back to the underlying ConnectionPool (or other kind of Connection-Provider, e.g. closes the Connection).
	 */
	void release(final Connection $connection);

	/**
	 * Selects all objects of a certain $class from the corresponding Table.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class);
	
	/**
	 * Like select(), but with a $limit.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, int $limit);
	
	/**
	 * Like select(), but with $offset and $limit.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, int $offset, int $limit);
	
	/**
	 * 
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, final String $query, Object... $values);
	
	/**
	 * 
	 */
	RecordSet<ActiveRecord<?>> query(final String $preparedQuery, Object... $values);
	
	/**
	 * 
	 */
	ActiveRecord<?> querySingle(final String $preparedQuery, Object... $values);
	
	/**
	 * 
	 */
	<T extends ActiveRecord<?>> T selectById(Class<T> $class, int $id);
	
	/**
	 * 
	 */
	<T extends ActiveRecord<?>> T create(final Class<T> $class);

	/**
	 * Drops all Tables belonging to this Schema from the current Database.
	 */
	void dropDatabase();
	
	/**
	 * Creates all Tables belonging to this Schema within the current Database.
	 */
	void createDatabase();

	/**
	 * Like select() with a $limit of 1, returns null in case of an empty result set.
	 */
	<T extends ActiveRecord<?>> T selectOne(Class<T> $class, String $query, Object... $values);
	
	/**
	 * Returns the Schema-object that describes this particular Database.
	 */
	Schema getSchema();
	
	/**
	 * Retrieves the Database-oddities specific to the current database.
	 */
	DatabaseExtravaganza getDatabaseExtravaganza();
	
	/**
	 * Closes this DatabaseAccess-object (e.g. releases all internally pooled connections).
	 */
	void close();
}
