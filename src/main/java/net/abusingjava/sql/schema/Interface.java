package net.abusingjava.sql.schema;

import java.util.Map;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.beans.AbusingBeans;
import net.abusingjava.sql.DatabaseSQL;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class Interface {

	final Class<?> $interface;
	final String $name;
	final String $simpleName;
	final String $sqlName;
	final Property[] $properties;
	final Schema $parent;
	
	Interface(final Schema $parent, final Class<?> $class) {
		if ($class == null) {
			throw new IllegalArgumentException("$interface may not be null.");
		} else if (!$class.isInterface()) {
			throw new IllegalArgumentException("$interface must be an interface");
		}
		this.$parent = $parent;
		this.$interface = $class;
		this.$name = $class.getCanonicalName();
		this.$simpleName = $class.getSimpleName();
		this.$sqlName = DatabaseSQL.makeSQLName($simpleName);
		
		Map<String,Class<?>> $p = AbusingBeans.getProperties($class);
		$p.remove("Id");
		$properties = new Property[$p.size()];
		int $i = 0;
		for (String $key : $p.keySet()) {
			$properties[$i++] = new Property(this, $class, $key);
		}
	}
	
	public String getName() {
		return $name;
	}
	
	public String getSqlName() {
		return $sqlName;
	}
	
	public Property[] getProperties() {
		return $properties;
	}

	public Property getProperty(final String $name) {
		for (Property $p : $properties) {
			if ($p.getName().equals($name)) {
				return $p;
			}
		}
		return null;
	}

	@Override
	public boolean equals(final Object $o) {
		if ($o instanceof Interface) {
			return ((Interface) $o).$interface.equals(this.$interface);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return $interface.hashCode();
	}
}
