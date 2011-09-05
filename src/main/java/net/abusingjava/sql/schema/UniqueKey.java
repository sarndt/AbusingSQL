package net.abusingjava.sql.schema;

import java.util.List;

import net.abusingjava.AbusingStrings;

public class UniqueKey {

	final String $name;
	final Property[] $properties;
	
	UniqueKey(final Property... $properties) {
		this.$name = null;
		this.$properties = $properties;
	}
	
	UniqueKey(final String $name, final List<Property> $properties) {
		this.$name = $name;
		this.$properties = $properties.toArray(new Property[$properties.size()]);
	}

	public Property[] getProperties() {
		return $properties;
	}
	
	public String getName() {
		return $name;
	}
	
	@Override
	public String toString() {
		return "(" + AbusingStrings.implode(", ", $properties) + ")";
	}
}
