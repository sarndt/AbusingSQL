package net.abusingjava.sql.v1.impl;

import java.sql.*;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.sql.v1.*;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public class GenericDatabaseAccess extends AbstractDatabaseAccess {

	final private ConnectionProvider $connectionProvider;
	final private DatabaseExtravaganza $databaseExtravaganza;
	final private ActiveRecordFactory $activeRecordFactory = new GenericActiveRecordFactory();
	final private RecordSetFactory $recordSetFactory = new GenericRecordSetFactory();
	
	public GenericDatabaseAccess(
			final Schema $schema,
			final ConnectionProvider $connectionProvider,
			final DatabaseExtravaganza $databaseExtravaganza) {
		super($schema);
		this.$connectionProvider = $connectionProvider;
		this.$databaseExtravaganza = $databaseExtravaganza;
	}

	@Override
	public <T extends ActiveRecord<T>> T create(final Class<T> $class) {
		return $activeRecordFactory.create($class);
	}

	@Override
	public <T extends ActiveRecord<T>> T selectById(
			final Transaction $transaction,
			final Class<T> $class,
			final int $id) {
		String $query = $databaseExtravaganza.getSelectByIdQuery($class);
		return selectOne($class, $query, $id);
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Transaction $transaction,
			final Class<T> $class,
			final int $offset,
			final int $limit, final Class<?>... $joinClasses) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		String $query = $databaseExtravaganza.getSelectQuery($class, $offset, $limit, $joinClasses);
		try {
			PreparedStatement $stmt = $connection.prepareStatement($query);
			ResultSet $result = $stmt.executeQuery();
			return $recordSetFactory.createFromResultSet($class, $result);
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> select(
			final Transaction $transaction,
			final Class<T> $class,
			final String $query,
			final Object... $args) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		try {
			PreparedStatement $stmt = $connection.prepareStatement($query);
			for (int $i = 0; $i < $args.length; $i++) {
				$databaseExtravaganza.setValue($stmt, $i+1, $args[$i]);
			}
			ResultSet $result = $stmt.executeQuery();
			return $recordSetFactory.createFromResultSet($class, $result);
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			final Transaction $transaction,
			final T $example,
			final int $offset,
			final int $limit,
			final Class<?>... $joinClasses) {
		String $query = $databaseExtravaganza.getSelectQuery($example, $offset, $limit, $joinClasses);
		return select($transaction, (Class<T>) $example.getClass(), $query, $databaseExtravaganza.getExampleValues($example));
	}

	@Override
	public Transaction beginTransaction() {
		return new GenericTransaction($connectionProvider);
	}

	@Override
	public RecordSet<ActiveRecord<?>> query(final Transaction $transaction, final String $preparedQuery, final Object... $values) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		try {
			PreparedStatement $stmt = $connection.prepareStatement($preparedQuery);
			for (int $i = 0; $i < $values.length; $i++) {
				$databaseExtravaganza.setValue($stmt, $i+1, $values[$i]);
			}
			ResultSet $result = $stmt.executeQuery();
			return $recordSetFactory.createFromResultSet((Class<ActiveRecord<?>>) null, $result);
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
	}

	@Override
	public DatabaseAccess exec(final Transaction $transaction, final String $preparedQuery, final Object... $values) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		try {
			PreparedStatement $stmt = $connection.prepareStatement($preparedQuery);
			for (int $i = 0; $i < $values.length; $i++) {
				$databaseExtravaganza.setValue($stmt, $i+1, $values[$i]);
			}
			$stmt.execute();
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
		return this;
	}

	@Override
	public DatabaseAccess exec(final Transaction $transaction, final String $query) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		Statement $statement;
		try {
			$statement = $connection.createStatement();
		} catch (SQLException $exc) {
			throw new DatabaseException("Failed to create Statement.", $exc);
		}
		try {
			$statement.execute($query);
		} catch (SQLException $exc) {
			throw new DatabaseException("Failed to execute previously created Statement.", $exc);
		}
		return this;
	}

	@Override
	public DatabaseAccess createTables() {
		$databaseExtravaganza.createTables(this);
		return this;
	}

	@Override
	public DatabaseAccess updateSchema() {
		$databaseExtravaganza.updateSchema(this);
		return this;
	}

	@Override
	public <T extends ActiveRecord<?>> DatabaseAccess flush(final Class<T>... $classes) {
		$databaseExtravaganza.flushTables(this, $classes);
		return this;
	}

	@Override
	public <T extends ActiveRecord<?>> DatabaseAccess drop(final Class<T>... $classes) {
		$databaseExtravaganza.dropTables(this, $classes);
		return this;
	}

	@Override
	public DatabaseAccess dropSchema() {
		$databaseExtravaganza.dropSchema(this);
		return this;
	}
}
