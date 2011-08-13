package net.abusingjava.sql.impl;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class GenericDatabaseAccessFactory extends AbstractDatabaseAccessFactory {
	
	public GenericDatabaseAccessFactory(final Class<?>... $classes) {
		super();
		for (Class<?> $class : $classes) {
			$schema.addInterface($class);
		}
	}

}
