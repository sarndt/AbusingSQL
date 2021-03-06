package net.abusingjava.sql;

import java.util.Date;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
public interface Entity<T> extends ActiveRecord<T> {

	Date getLastModified();
	T setLastModified(Date $datetime);
	
	Date getCreated();
	T setCreated(Date $datetime);
	
}
