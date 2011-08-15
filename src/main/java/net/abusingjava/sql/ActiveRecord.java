package net.abusingjava.sql;

import java.sql.Connection;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.beans.SupportsPropertyChangeEvents;

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
	 * Speichert Änderungen und speichert auch Objekte die zu diesem in Beziehung stehen.
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
	 * 
	 */
	void delete();
	
	/**
	 * 
	 */
	void delete(Connection $c);

	/**
	 * 
	 */
	boolean exists();
	
	/**
	 * 
	 */
	boolean hasChanges();
	
	/**
	 * 
	 */
	Object get(String $column);
	
	/**
	 * 
	 */
	T set(String $column, Object $value);
	
}
