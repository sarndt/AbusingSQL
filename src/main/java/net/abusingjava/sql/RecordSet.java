package net.abusingjava.sql;

import net.abusingjava.SupportsPropertyChangeEvents;

public interface RecordSet<T extends ActiveRecord<?>> extends SetList<T>, SupportsPropertyChangeEvents {

	void saveChanges();
	
	void discardChanges();

	void deleteAll();
}
