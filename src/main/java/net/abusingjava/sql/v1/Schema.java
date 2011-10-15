package net.abusingjava.sql.v1;

public interface Schema {

	<T extends ActiveRecord<?>> Class<T>[] getClasses();
	
}
