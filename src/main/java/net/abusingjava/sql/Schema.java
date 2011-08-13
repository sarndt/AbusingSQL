package net.abusingjava.sql;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class Schema {

	final List<Interface> $interfaces = new LinkedList<Interface>();
	final Set<ManyToMany> $manyToMany = new HashSet<ManyToMany>();
	
	
	public void addInterface(final Class<?> $interface) {
		if (getInterface($interface.getCanonicalName()) == null) {
			Interface $i = new Interface(this, $interface);
			$interfaces.add($i);
			
			for (Property $p : $i.getProperties()) {
				if ($p.getGenericType() != null) {
					addInterface($p.getGenericType());
				} else if ($p.isManyPart()) {
					addInterface($p.getJavaType());
				}
			}
			for (Property $p : $i.getProperties()) {
				if ($p.getGenericType() != null) {
					String $fromProperty = $p.getName();
					Interface $to = getInterface($p.getGenericType().getCanonicalName());
					for (Property $p2 : $to.getProperties()) {
						if ($i.$interface.equals($p2.getGenericType())) {
							$manyToMany.add(new ManyToMany(this, $i.getName(), $fromProperty, $to.getName(), $p2.getName()));
						}
					}
				}
			}
		}
	}
	
	public Interface[] getInterfaces() {
		return $interfaces.toArray(new Interface[$interfaces.size()]);
	}
	
	public Interface getInterface(final String $canonicalName) {
		for (Interface $i : $interfaces) {
			if ($i.getName().equals($canonicalName)) {
				return $i;
			}
		}
		return null;
	}
	
	public ManyToMany[] getManyToManyRelationships() {
		return $manyToMany.toArray(new ManyToMany[$manyToMany.size()]);
	}
}
