package net.abusingjava.sql;

import java.sql.Connection;

import net.abusingjava.Author;
import net.abusingjava.Experimental;
import net.abusingjava.Version;
import net.abusingjava.sql.schema.Schema;

/**
 * A DatabaseAccess-object is the central object which gives you access to the Database.
 */
@Author("Julian Fleischer")
@Version("2011-09-05")
public interface DatabaseAccess {

	/**
	 * Retrieves a Connection from the underlying ConnectionPool (or other kind
	 * of Connection-Provider, depending on the Implementation).
	 */
	Connection getConnection();

	/**
	 * Gives a Connection back to the underlying ConnectionPool (or other kind
	 * of Connection-Provider, e.g. closes the Connection).
	 */
	void release(final Connection $connection);

	/**
	 * Selects all objects of a certain $class from the corresponding Table.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class);

	/**
	 * Like {@link #select(Class)}, but with a $limit.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, int $limit);

	/**
	 * Like {@link #select(Class)}, but with $offset and $limit.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, int $offset, int $limit);

	/**
	 * Performs a custom $query with the given $values - the Result will be a RecordSet containing objects of the given $class.
	 */
	<T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, final String $query, Object... $values);

	/**
	 * Performs a custom $query with the given $values - the Result will be a RecordSet containing objects of type <code>ActiveRecord&lt?></code>.
	 */
	RecordSet<ActiveRecord<?>> query(final String $preparedQuery, Object... $values);

	/**
	 * Performs a custom $query and returns the first result in the resulting RecordSet (or null if there was no result).
	 */
	ActiveRecord<?> querySingle(final String $preparedQuery, Object... $values);

	/**
	 * Selects a single object of type $class by its $id.
	 */
	<T extends ActiveRecord<?>> T selectById(Class<T> $class, int $id);

	/**
	 * Creates a new ActiveRecord of the given $class.
	 * Please note that the object is instantiated in the Database
	 * only if you call {@link ActiveRecord#saveChanges() saveChanges()}
	 * on that object.
	 */
	<T extends ActiveRecord<?>> T create(final Class<T> $class);

	/**
	 * Creates a new ActiveRecord of the given $class and a known $id - <b>Please do use for updating objects only!</b>.
	 */
	@Experimental
	<T extends ActiveRecord<?>> T create(final Class<T> $class, int $id);

	/**
	 * Drops all Tables belonging to this Schema from the current Database. <br>
	 * <br>
	 * Note: Tables NOT belonging to the current Schema defined by this
	 * DatabaseAccess are completely ignored. This means if the Database Access
	 * used to define the Database had MORE tables than the current
	 * DatabaseAccess used to access the database now, the tables not incl uded
	 * in the current DatabaseAccess will be ignored. <br>
	 * <br>
	 * If you wish to drop everything use {@link #dropAllTablesInDatabase()
	 * dropAllTablesInDatabase()}.
	 */
	void dropDatabase();

	/**
	 * Drops the current DB and recreates it without tables afterwards. This is
	 * useful if you need to drop all tables in the DB, regardless of which
	 * Schema was used to generate your DatabaseAccess object. <br>
	 * <br>
	 * See also the JavaDoc for the method {@link #dropDatabase()
	 * dropDatabase()}. <br>
	 * <br>
	 * Note: User must have DROP-Privilege or be the owner of the Database!
	 * 
	 */
	void dropAllTablesInDatabase();

	/**
	 * Creates all Tables belonging to this Schema within the current Database.
	 */
	void createDatabase();

	/**
	 * Like select() with a $limit of 1, returns null in case of an empty result
	 * set.
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
	 * Closes this DatabaseAccess-object (e.g. releases all internally pooled
	 * connections).
	 */
	void close();
}
