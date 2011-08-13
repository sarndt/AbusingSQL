package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-08")
public class GenericDatabaseAccessFactory extends AbstractDatabaseAccessFactory {
	
	@SuppressWarnings("unchecked")
	public GenericDatabaseAccessFactory(Class<?>... $classes) {
		super();
		for (Class<?> $class : $classes) {
			$schema.addInterface((Class<? extends ActiveRecord<?>>) $class);
		}
	}

}
