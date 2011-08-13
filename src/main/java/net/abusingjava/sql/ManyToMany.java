package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class ManyToMany {

	final String $from;
	final String $fromProperty;
	final String $to;
	final String $toProperty;
	final Schema $parent;
	
	ManyToMany(final Schema $parent, final String $from, final String $fromProperty,
			final String $to, final String $toProperty) {
		this.$parent = $parent;
		this.$from = $from;
		this.$fromProperty = $fromProperty;
		this.$to = $to;
		this.$toProperty = $toProperty;
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
		return $parent.getInterface($from).getProperty($fromProperty);
	}
	
	public Property getToProperty() {
		return $parent.getInterface($to).getProperty($toProperty);
	}
	
	@Override
	public boolean equals(final Object $o) {
		if ($o instanceof ManyToMany) {
			return ($from.equals(((ManyToMany) $o).$from) && $to.equals(((ManyToMany) $o).$to)
					&& $fromProperty.equals(((ManyToMany) $o).$fromProperty) && $toProperty.equals(((ManyToMany) $o).$toProperty))
					|| ($from.equals(((ManyToMany) $o).$to) && $to.equals(((ManyToMany) $o).$from)
					&& $fromProperty.equals(((ManyToMany) $o).$toProperty) && $toProperty.equals(((ManyToMany) $o).$fromProperty));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return $from.hashCode() ^ $to.hashCode();
	}
	
	@Override
	public String toString() {
		return $from + '#' + $fromProperty + " -> " + $to + '#' + $toProperty;
	}
}
