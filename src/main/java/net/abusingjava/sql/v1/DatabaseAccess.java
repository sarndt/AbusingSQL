package net.abusingjava.sql.v1;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface DatabaseAccess {

	<T extends ActiveRecord<T>> T create(Class<T> $class);

	<T extends ActiveRecord<T>> T selectById(Class<T> $class, int $id);

	<T extends ActiveRecord<T>> T selectById(Transaction $transaction, Class<T> $class, int $id);

	<T extends ActiveRecord<T>> RecordSet<T> select(Class<T> $class, Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> select(Class<T> $class, int $limit, Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> select(
			Class<T> $class,
			int $offset,
			int $limit,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> select(Class<T> $class, String $query, Object... $args);

	<T extends ActiveRecord<T>> RecordSet<T> selectByExample(T $example, Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> selectByExample(T $example, int $limit, Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			T $example,
			int $offset,
			int $limit,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> T selectOne(Class<T> $class, Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> T selectOne(Class<T> $class, String $query, Object... $args);

	<T extends ActiveRecord<T>> RecordSet<T> select(
			Transaction $transaction,
			Class<T> $class,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> select(
			Transaction $transaction,
			Class<T> $class,
			int $limit,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> select(
			Transaction $transaction,
			Class<T> $class,
			int $offset,
			int $limit,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> select(
			Transaction $transaction,
			Class<T> $class,
			String $query,
			Object... $args);

	<T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			Transaction $transaction,
			T $example,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			Transaction $transaction,
			T $example,
			int $limit,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			Transaction $transaction,
			T $example,
			int $offset,
			int $limit,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> T selectOne(
			Transaction $transaction,
			Class<T> $class,
			Class<?>... $joinClasses);

	<T extends ActiveRecord<T>> T selectOne(
			Transaction $transaction,
			String $query,
			Object... $args);

	Transaction beginTransaction();

	RecordSet<ActiveRecord<?>> query(String $preparedQuery, Object... $values);

	ActiveRecord<?> querySingle(String $preparedQuery, Object... $values);

	DatabaseAccess exec(String $preparedQuery, Object... $values);

	DatabaseAccess exec(String $query);

	DatabaseAccess exec(Transaction $transaction, String $query);

	RecordSet<ActiveRecord<?>> query(Transaction $transaction, String $preparedQuery, Object... $values);

	ActiveRecord<?> querySingle(Transaction $transaction, String $preparedQuery, Object... $values);

	DatabaseAccess exec(Transaction $transaction, String $preparedQuery, Object... $values);

	DatabaseAccess createTables();

	DatabaseAccess updateSchema();

	<T extends ActiveRecord<?>> DatabaseAccess flush(Class<T>... $class);

	<T extends ActiveRecord<?>> DatabaseAccess drop(Class<T>... $class);

	DatabaseAccess flushTables();

	DatabaseAccess dropTables();

	DatabaseAccess dropSchema();

	Schema getSchema();
}
