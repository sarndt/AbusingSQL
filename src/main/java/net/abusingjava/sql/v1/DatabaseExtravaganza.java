package net.abusingjava.sql.v1;

import java.sql.PreparedStatement;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface DatabaseExtravaganza {
	
	<T extends ActiveRecord<T>> String getSelectQuery(Class<T> $class, int $offset, int $limit, Class<?>... $joinClasses);
	
	<T extends ActiveRecord<T>> String getSelectQuery(T $example, int $offset, int $limit, Class<?>... $joinClasses);
	
	<T extends ActiveRecord<T>> Object[] getExampleValues(T $example);
	
	<T extends ActiveRecord<T>> String getSelectByIdQuery(Class<T> $class);
	
	void setValue(final PreparedStatement $stmt, final int $index, final Object $value);
	
	void createTables(DatabaseAccess $databaseAccess);

	void updateSchema(DatabaseAccess $databaseAccess);

	void dropSchema(DatabaseAccess $databaseAccess);
	
	void dropTables(DatabaseAccess $databaseAccess, Class<?>[] $tables);

	void flushTables(DatabaseAccess $databaseAccess, Class<?>[] $tables);
}
