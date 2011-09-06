package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

/**
 * An interface which extends this interface can be used to mix in external code into an ActiveRecord handler.
 * An implementation of a Mixin must also implement this interface, as it implements the
 * interface which defines the Mixin-Methods.
 * 
 * @see MixinHandler
 */
@Author("Julian Fleischer")
@Version("2011-09-06")
public interface Mixin<T extends ActiveRecord<?>> {

	/**
	 * Most Mixins need access to the database, this method is used internally
	 * to publish the database access object to the ActiveRecord.
	 */
	void setDatabaseAccess(DatabaseAccess $dbAccess);
	
	/**
	 * Sets the ActiveRecord which this Mixin is part of.
	 */
	void setActiveRecord(T $activeRecord);
}
