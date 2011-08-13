package net.abusingjava.sql;

import java.util.Deque;
import java.util.List;
import java.util.Set;

public interface RecordSet<T extends ActiveRecord<?>> extends Deque<T>, Set<T>, List<T> {

	void saveChanges();

	void deleteAll();
	
}
