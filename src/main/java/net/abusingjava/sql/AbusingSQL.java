package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.impl.GenericDatabaseAccessFactory;

@Author("Julian Fleischer")
@Version("2011-08-16")
final public class AbusingSQL {

	AbusingSQL() {}
	
	public static DatabaseAccessFactory newDatabaseAccessFactory(final String $name) {
		try {
			return (DatabaseAccessFactory) Class.forName($name).newInstance();
		} catch (Exception $exc) {
			throw new RuntimeException($exc);
		}
	}

	public static <T extends DatabaseAccessFactory> T newDatabaseAccessFactory(final Class<T> $class) {
		try {
			return $class.newInstance();
		} catch (Exception $exc) {
			throw new RuntimeException($exc);
		}
	}

	public static DatabaseAccessFactory newDatabaseAccessFactory(final DatabaseExtravaganza $extravaganza, final Class<?>... $classes) {
		return new GenericDatabaseAccessFactory($extravaganza, $classes);
	}
}
