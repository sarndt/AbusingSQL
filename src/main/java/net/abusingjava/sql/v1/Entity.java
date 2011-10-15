package net.abusingjava.sql.v1;

import java.util.Date;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface Entity<T> extends ActiveRecord<T> {

	Date getLastModified();
	T setLastModified(Date $datetime);
	
	Date getCreated();
	T setCreated(Date $datetime);
	
}
