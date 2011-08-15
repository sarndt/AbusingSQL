package net.abusingjava.sql;

public interface RecordSet<T extends ActiveRecord<?>> extends SetList<T> {

	void saveChanges();

	void deleteAll();
	
}
