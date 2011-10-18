package net.abusingjava.sql;

import java.util.List;

import net.abusingjava.event.OffersPropertyChangeEvents;

public interface RecordSet<T extends ActiveRecord<?>> extends List<T>, OffersPropertyChangeEvents {

	void saveChanges();
	
	void discardChanges();
	
	boolean hasChanges();

	void deleteAll();
	
	T getFirst();
	
	T getById(int $id);
}
