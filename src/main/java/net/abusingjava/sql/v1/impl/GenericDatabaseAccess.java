package net.abusingjava.sql.v1.impl;

import java.math.BigDecimal;
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
	//final private RecordSetFactory $recordSetFactory = new GenericRecordSetFactory();
	
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
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		// TODO Auto-generated method stub
		return null;
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
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
		return null;
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
				setValue($stmt, $i+1, $args[$i]);
			}
			ResultSet $result = $stmt.executeQuery();
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
		return null;
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> selectByExample(
			final Transaction $transaction,
			final T $example,
			final int $offset,
			final int $limit,
			final Class<?>... $joinClasses) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		// TODO Auto-generated method stub
		return null;
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
				setValue($stmt, $i+1, $values[$i]);
			}
			ResultSet $result = $stmt.executeQuery();
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
		return null;
	}

	@Override
	public DatabaseAccess exec(final Transaction $transaction, final String $preparedQuery, final Object... $values) {
		Connection $connection = $transaction == null
				? $connectionProvider.getConnection()
				: $transaction.getConnection();
		try {
			PreparedStatement $stmt = $connection.prepareStatement($preparedQuery);
			for (int $i = 0; $i < $values.length; $i++) {
				setValue($stmt, $i+1, $values[$i]);
			}
			$stmt.execute();
		} catch (SQLException $exc) {
			throw new DatabaseException("", $exc);
		}
		return this;
	}

	private void setValue(final PreparedStatement $stmt, final int $index, final Object $object) throws SQLException {
		if ($object instanceof Integer) {
			$stmt.setInt($index, (Integer) $object);
		} else if ($object instanceof Long) {
			$stmt.setLong($index, (Long) $object);
		} else if ($object instanceof Double) {
			$stmt.setDouble($index, (Double) $object);
		} else if ($object instanceof Float) {
			$stmt.setFloat($index, (Float) $object);
		} else if ($object instanceof String) {
			$stmt.setString($index, $object.toString());
		} else if ($object instanceof Date) {
			$stmt.setTimestamp($index, new Timestamp(((Date) $object).getTime()));
		} else if ($object instanceof Boolean) {
			$stmt.setBoolean($index, (Boolean) $object);
		} else if ($object instanceof BigDecimal) {
			$stmt.setBigDecimal($index, (BigDecimal) $object);
		} else if ($object instanceof Short) {
			$stmt.setShort($index, (Short) $object);
		}
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
