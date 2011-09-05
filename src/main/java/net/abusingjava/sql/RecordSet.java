package net.abusingjava.sql;

import net.abusingjava.SupportsPropertyChangeEvents;

import org.jdesktop.observablecollections.ObservableList;

public interface RecordSet<T extends ActiveRecord<?>> extends SetList<T>, SupportsPropertyChangeEvents, ObservableList<T> {

	void saveChanges();
	
	void discardChanges();

	void deleteAll();
	
	void installPropertyChangeListeners();
}
