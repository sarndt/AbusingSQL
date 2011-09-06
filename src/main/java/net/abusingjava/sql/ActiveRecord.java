package net.abusingjava.sql;

import java.sql.Connection;
import java.util.Map;

import net.abusingjava.Author;
import net.abusingjava.SupportsPropertyChangeEvents;
import net.abusingjava.Version;

/**
 * 
 */
@Author("Julian Fleischer")
@Version("2011-08-15")
public interface ActiveRecord<T> extends SupportsPropertyChangeEvents {

	/**
	 * 
	 */
	Integer getId();
	
	/**
	 * 
	 */
	T saveChanges();
	
	/**
	 * 
	 * 
	 */
	T saveChanges(Connection $c);
	
	/**
	 * 
	 */
	T saveChanges(int $depth);
	
	/**
	 * 
	 */
	T saveChanges(Connection $c, int $depth);
	
	/**
	 * 
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
