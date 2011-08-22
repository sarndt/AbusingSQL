package net.abusingjava.sql;

import com.sun.jndi.url.dns.dnsURLContext;
import java.sql.Connection;

import javax.swing.JList.DropLocation;
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
	 * 
	 */
	<T extends ActiveRecord<?>> T create(final Class<T> $class, int $id);

	/**
	 * Drops all Tables belonging to this Schema from the current Database.
         * <br><br>
         * Note: Tables NOT belonging to the current Schema defined by this
         * DatabaseAccess are completely ignored. This means if the Database
         * Access used to define the Database had MORE tables than the current
         * DatabaseAccess used to access the database now, the tables not incl
         * uded in the current DatabaseAccess will be ignored.
         * <br><br>
         * If you wish to drop everything
         * use {@link #dropAllTablesInDatabase() dropAllTablesInDatabase()}.
	 */
	void dropDatabase();
        
        /**
         * Drops the current DB and recreates it without tables afterwards.
         * This is useful if you need to drop all tables in the DB, regardless
         * of which Schema was used to generate your DatabaseAccess object.
         * <br><br>
         * See also the JavaDoc for the method {@link #dropDatabase() dropDatabase()}.
         * <br><br>
         * Note: User must have DROP-Privilege or be the owner of the Database!
         * 
         */
        void dropAllTablesInDatabase();
	
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
