package net.abusingjava.sql.schema;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-15")
public class ManyToMany {

	final String $from;
	final String $fromPropertyName;
	Property $fromProperty = null;
	final String $to;
	final String $toPropertyName;
	Property $toProperty = null;
	
	final Schema $parent;
	
	ManyToMany(final Schema $parent, final String $from, final String $fromProperty,
			final String $to, final String $toProperty) {
		this.$parent = $parent;
		this.$from = $from;
		this.$fromPropertyName = $fromProperty;
		this.$to = $to;
		this.$toPropertyName = $toProperty;
	}
	
	public Schema getSchema() {
		return $parent;
	}
	
	public Interface getFrom() {
		return $parent.getInterface($from);
	}
	
	public Interface getTo() {
		return $parent.getInterface($to);
	}
	
	public Property getFromProperty() {
		if ($fromProperty == null) {
			$fromProperty = $parent.getInterface($from).getProperty($fromPropertyName);
		}
		return $fromProperty; 
	}
	
	public Property getToProperty() {
		if ($toProperty == null) {
			$toProperty = $parent.getInterface($to).getProperty($toPropertyName); 
		}
		return $toProperty;
	}
	
	@Override
	public boolean equals(final Object $o) {
		if ($o instanceof ManyToMany) {
			return ($from.equals(((ManyToMany) $o).$from) && $to.equals(((ManyToMany) $o).$to)
					&& $fromPropertyName.equals(((ManyToMany) $o).$fromPropertyName) && $toPropertyName.equals(((ManyToMany) $o).$toPropertyName))
					|| ($from.equals(((ManyToMany) $o).$to) && $to.equals(((ManyToMany) $o).$from)
					&& $fromPropertyName.equals(((ManyToMany) $o).$toPropertyName) && $toPropertyName.equals(((ManyToMany) $o).$fromPropertyName));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return $from.hashCode() ^ $to.hashCode();
	}
	
	@Override
	public String toString() {
		return $from + '#' + $fromPropertyName + " -> " + $to + '#' + $toPropertyName;
	}
}
