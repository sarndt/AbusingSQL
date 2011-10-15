package net.abusingjava.sql.v1;

import java.util.List;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface RecordSet<T extends ActiveRecord<?>> extends List<T> {

	T getFirst();
	
	T getLast();

	int saveChanges();

	int saveChanges(Transaction $transaction);
	
	int deleteAll();

	int deleteAll(Transaction $transaction);

	boolean hasChanges();
	
	void discardChanges();
}
