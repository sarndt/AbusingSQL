package net.abusingjava.sql.impl;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.DatabaseExtravaganza;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class GenericDatabaseAccessFactory extends AbstractDatabaseAccessFactory {
	
	public GenericDatabaseAccessFactory(final DatabaseExtravaganza $extravaganza, final Class<?>... $classes) {
		super($extravaganza);
		for (Class<?> $class : $classes) {
			$schema.addInterface($class);
		}
	}

}
