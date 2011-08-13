package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-10")
public interface DatabaseAccessFactory {

	void setPoolsize(final int $size);

	DatabaseAccess newDatabaseAccess(final String $url, final String $user, final String $password);

	Schema getSchema();

}