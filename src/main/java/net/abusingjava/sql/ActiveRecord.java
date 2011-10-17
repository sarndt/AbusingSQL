package net.abusingjava.sql;

import java.sql.Connection;
import java.util.Map;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.event.OffersPropertyChangeEvents;

/**
 * An ActiveRecord is an object which maintains its state in the database by itself.
 */
@Author("Julian Fleischer")
@Version("2011-08-15")
public interface ActiveRecord<T> extends OffersPropertyChangeEvents {

	/**
	 * Returns the unique identifier of this object or null, if it does not exist in the database.
	 */
	Integer getId();
	
	/**
	 * Saves the changes which are done to this ActiveRecord. Any ActiveRecords which do not exist yet
	 * but have been assigned to this object via a Many-to-Many-Relationship will also be saved (i.e. created)
	 * if this method is invoked.
	 */
	T saveChanges();
	
	/**
	 * Like {@link #saveChanges()}, but used the given connection for the task. This is mostly used internally
	 * for using the same Connection within a Transaction.
	 */
	T saveChanges(Connection $c);
	
	/**
	 * Like {@link #saveChanges()}, but upto a certain depth also invokes <code>saveChanges()</code> on
	 * referenced objects.
	 */
	T saveChanges(int $depth);
	
	/**
	 * A combination of {@link #saveChanges(Connection)} and {@link #saveChanges(Connection, int)}.
	 */
	T saveChanges(Connection $c, int $depth);
	
	/**
	 * Restores the state of the object as it was when freshly fetched from the database (or created).
	 */
	T discardChanges();
	
	/**
	 * Deletes this ActiveRecord from the Database (if it exists).
	 */
	void delete();
	
	/**
	 * Deletes this ActiveRecord from the Database (if it exists) using the given connection.
	 */
	void delete(Connection $c);

	/**
	 * Checks whether this ActiveRecord exists in the Database.
	 */
	boolean exists();
	
	/**
	 * Checks whether this ActiveRecord has any changes.
	 */
	boolean hasChanges();
	
	/**
	 * Retrieves a column value by it’s SQL identifier.
	 */
	Object get(String $column);
	
	/**
	 * Sets a column value by it’s SQL identifier.
	 */
	T set(String $column, Object $value);
	
	/**
	 * Clears the internal cache (mostly these are references to other objects).
	 */
	T clearCache();
	
	/**
	 * Retrieves a list of all keys which are set in this ActiveRecord.
	 */
	String[] keys();
	
	/**
	 * Retrieves a Map of all values which are newly set on this object.
	 */
	Map<String,Object> newValues();
	
	/**
	 * Returns the DatabaseAccess that this ActiveRecord is u
	 */
	DatabaseAccess databaseAccess();
}
