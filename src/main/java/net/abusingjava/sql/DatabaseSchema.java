package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class DatabaseSchema {

	public static class Entity {

		private final String $name;
		
		Entity(final String $name) {
			this.$name = $name;
		}
		
		public String getName() {
			return $name;
		}
		
	}
	
	public static class Property {
		
		private final String $name;
		
		Property(final String $name) {
			this.$name = $name;
		}
		
		public String getName() {
			return $name;
		}
		
	}
	
	static abstract class Relationship {
		
	}
	
	public static class ManyToMany extends Relationship {
		
	}
	
	public static class OneToMany extends Relationship {
		
	}
	
	public static class Table {
		
		public Column[] getColumns() {
			return null;
		}
		
	}
	
	public static class Column {

		private final String $name;
		
		Column(final String $name) {
			this.$name = $name;
		}
		
		public String getName() {
			return $name;
		}
		
	}
	
	public static abstract class Constraint {
		
	}
	
	public static class ForeignKey extends Constraint {
		
	}
	
	public static class UniqueKey extends Constraint {
		
	}
	
	public static class PrimaryKey extends Constraint {
		
	}
	
	public DatabaseSchema() {
		
	}
	
	public void addInterface(final Class<? extends ActiveRecord<?>> $interface) {
		if ($interface == null) {
			throw new IllegalArgumentException("$interface may not be null.");
		} else if (!$interface.isInterface()) {
			throw new IllegalArgumentException("$interface must be an interface");
		}
		add($interface);
	}
	
	private void add(@SuppressWarnings("unused") final Class<? extends ActiveRecord<?>> $interface) {
		
	}
	
	public Table[] getTables() {
		return null;
	}
	
	void createDatabase(@SuppressWarnings("unused") final ConnectionPool $pool) {
		
	}
	
	void dropDatabase(@SuppressWarnings("unused") final ConnectionPool $pool) {
		
	}
}
