package net.abusingjava.sql;

import java.util.List;

import net.abusingjava.SupportsPropertyChangeEvents;

public interface RecordSet<T extends ActiveRecord<?>> extends List<T>, SupportsPropertyChangeEvents {

	void saveChanges();
	
	void discardChanges();
	
	boolean hasChanges();

	void deleteAll();
	
	T getFirst();
}
