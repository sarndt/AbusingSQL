package net.abusingjava.sql.v1.impl;

import net.abusingjava.sql.v1.*;

public abstract class AbstractDatabaseAccess implements DatabaseAccess {

	private final Schema $schema;
	
	public AbstractDatabaseAccess(final Schema $schema) {
		this.$schema = $schema;
	}
	
	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			final T $example,
			final Class<?>... $joinClasses) {
		return selectByExample($example, 0, -1, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			final T $example,
			final int $limit,
			final Class<?>... $joinClasses) {
		return selectByExample($example, 0, $limit, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> T selectOne(
			final Class<T> $class,
			final Class<?>... $joinClasses) {
		return select($class, $joinClasses).getFirst();
	}

	@Override
	public <T extends ActiveRecord<T>> T selectOne(
			final Class<T> $class,
			final String $query,
			final Object... $args) {
		return select($class, $query, $args).getFirst();
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			final Transaction $transaction,
			final T $example,
			final Class<?>... $joinClasses) {
		return selectByExample($transaction, $example, 0, -1, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(final Transaction $transaction, final T $example, final int $limit,
			final Class<?>... $joinClasses) {
		return selectByExample($transaction, $example, 0, $limit, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> T selectOne(final Transaction $transaction, final String $query, final Object... $args) {
		return selectOne($transaction, $query, $args);
	}

	@Override
	public ActiveRecord<?> querySingle(
			final String $preparedQuery,
			final Object... $values) {
		return query($preparedQuery, $values).getFirst();
	}

	@Override
	public DatabaseAccess flushTables() {
		return flush(getSchema().getClasses());
	}

	@Override
	public DatabaseAccess dropTables() {
		return drop(getSchema().getClasses());
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Class<T> $class,
			final Class<?>... $joinClasses) {
		return select($class, 0, -1, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Class<T> $class,
			final int $limit,
			final Class<?>... $joinClasses) {
		return select($class, 0, $limit, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Transaction $transaction,
			final Class<T> $class,
			final Class<?>... $joinClasses) {
		return select($transaction, $class, 0, -1, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Transaction $transaction,
			final Class<T> $class,
			final int $limit,
			final Class<?>... $joinClasses) {
		return select($transaction, $class, 0, $limit, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> T selectOne(
			final Transaction $transaction,
			final Class<T> $class,
			final Class<?>... $joinClasses) {
		return select($transaction, $class, $joinClasses).getFirst();
	}

	@Override
	public <T extends ActiveRecord<T>> T selectById(final Class<T> $class, final int $id) {
		return selectById(null, $class, $id);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Class<T> $class,
			final int $offset,
			final int $limit,
			final Class<?>... $joinClasses) {
		return select(null, $class, $offset, $limit, $joinClasses);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Class<T> $class,
			final String $query,
			final Object... $args) {
		return select(null, $class, $query, $args);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			final T $example,
			final int $offset,
			final int $limit,
			final Class<?>... $joinClasses) {
		return selectByExample($example, $offset, $limit, $joinClasses);
	}

	@Override
	public ActiveRecord<?> querySingle(
			final Transaction $transaction,
			final String $preparedQuery,
			final Object... $values) {
		return query($transaction, $preparedQuery, $values).getFirst();
	}

	@Override
	public RecordSet<ActiveRecord<?>> query(final String $preparedQuery, final Object... $values) {
		return query(null, $preparedQuery, $values);
	}

	@Override
	public DatabaseAccess exec(final String $preparedQuery, final Object... $values) {
		return exec(null, $preparedQuery, $values);
	}

	@Override
	public DatabaseAccess exec(final String $query) {
		return exec(null, $query);
	}

	@Override
	public Schema getSchema() {
		return $schema;
	}
}
