package net.abusingjava.sql.schema;

import java.util.*;
import java.util.Map.Entry;

import net.abusingjava.AbusingReflection;
import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.Action;
import net.abusingjava.sql.OnDelete;
import net.abusingjava.sql.impl.DatabaseSQL;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class Interface {

	final String $name;
	final String $simpleName;
	final String $sqlName;
	final Class<?> $interface;
	final Property[] $properties;
	final UniqueKey[] $uniqueKeys;
	final Schema $parent;
	final Action $onDelete;
	final Property $toStringProperty;
	
	Interface(final Schema $parent, final Class<?> $class) {
		if ($class == null) {
			throw new IllegalArgumentException("$interface may not be null.");
		} else if (!$class.isInterface()) {
			throw new IllegalArgumentException("$interface must be an interface (" + $class + ")");
		}
		this.$parent = $parent;
		this.$interface = $class;
		this.$name = $class.getCanonicalName();
		this.$simpleName = $class.getSimpleName();
		this.$sqlName = DatabaseSQL.makeSQLName($simpleName);
		
		Map<String,Class<?>> $p = AbusingReflection.properties($class);
		$p.remove("Id");
		$properties = new Property[$p.size()];
		int $i = 0;
		Map<String,List<Property>> $keys = new HashMap<String,List<Property>>();
		Property $toStringProperty = null;
		for (String $key : $p.keySet()) {
			Property $property = new Property(this, $class, $key);
			$properties[$i++] = $property;
			if ($property.isUnique()) {
				String $keyName = $property.getUniqueKeyName();
				if (!$keys.containsKey($keyName)) {
					$keys.put($keyName, new LinkedList<Property>());
				}
				$keys.get($keyName).add($property);
			}
			if ($property.isToStringProperty()) {
				$toStringProperty = $property;
			}
		}
		this.$toStringProperty = $toStringProperty;
		List<UniqueKey> $uniqueKeys = new LinkedList<UniqueKey>();
		for (Entry<String,List<Property>> $e : $keys.entrySet()) {
			if ($e.getKey().isEmpty()) {
				for (Property $property : $e.getValue()) {
					$uniqueKeys.add(new UniqueKey($property));
				}
			} else {
				$uniqueKeys.add(new UniqueKey($e.getKey(), $e.getValue()));
			}
		}
		this.$uniqueKeys = $uniqueKeys.toArray(new UniqueKey[$uniqueKeys.size()]);
		
		if ($interface.isAnnotationPresent(OnDelete.class)) {
			$onDelete = $interface.getAnnotation(OnDelete.class).value();
		} else {
			$onDelete = Action.NO_ACTION;
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
	
	public UniqueKey[] getUniqueKeys() {
		return $uniqueKeys;
	}
	
	public boolean hasToStringProperty() {
		return $toStringProperty != null;
	}
	
	public Property getToStringProperty() {
		return $toStringProperty;
	}

	public Property getProperty(final String $name) {
		for (Property $p : $properties) {
			if ($p.getName().equals($name)) {
				return $p;
			}
		}
		return null;
	}
	
	public Class<?> getJavaType() {
		return $interface;
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
