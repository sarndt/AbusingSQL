package net.abusingjava.sql.v1;

import java.sql.ResultSet;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface RecordSetFactory {

	<T extends ActiveRecord<T>> RecordSet<T> create(Class<T> $class);

	<T extends ActiveRecord<T>> RecordSet<T> createFromResultSet(Class<T> $class, ResultSet $resultSet);

}
