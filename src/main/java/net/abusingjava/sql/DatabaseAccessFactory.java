package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.schema.Schema;

@Author("Julian Fleischer")
@Version("2011-09-05")
public interface DatabaseAccessFactory {

	/**
	 * Sets the number of cached connections if the database-access-object uses connection pooling.
	 */
	void setPoolsize(final int $size);

	/**
	 * Create a new DatabaseAccess-object with the given credentials.
	 */
	DatabaseAccess newDatabaseAccess(final String $url, final String $user, final String $password);

	/**
	 * Retrieve the schema used by this DatabaseAccessFactory.
	 */
	Schema getSchema();

}