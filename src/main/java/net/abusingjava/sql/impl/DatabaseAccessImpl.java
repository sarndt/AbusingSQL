package net.abusingjava.sql.impl;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import net.abusingjava.AbusingStrings;
import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.*;
import net.abusingjava.sql.schema.Schema;

@Author("Julian Fleischer")
@Version("2011-09-06")
public class DatabaseAccessImpl implements DatabaseAccess {

	final ConnectionProvider $pool;
	final Schema $schema;
	final DatabaseExtravaganza $extravaganza;

	Connection $connection = null;

	DatabaseAccessImpl(final DatabaseExtravaganza $extravaganza,
			final ConnectionProvider $pool, final Schema $schema) {
		this.$extravaganza = $extravaganza;
		this.$pool = $pool;
		this.$schema = $schema;
	}

	Connection connection() throws SQLException {
		if ($connection == null) {
			return $pool.getConnection();
		}
		return $connection;
	}

	@Override
	public Connection getConnection() {
		try {
			return $pool.getConnection();
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

	@Override
	public void release(final Connection $connection) {
		$pool.release($connection);
	}

	@Override
	public <T extends ActiveRecord<?>> T create(final Class<T> $class) {
		@SuppressWarnings("unchecked")
		T $instance = (T) Proxy.newProxyInstance($class.getClassLoader(),
				new Class<?>[] { $class }, new ActiveRecordHandler(this,
						$schema.getInterface($class)));
		return $instance;
	}

	@Override
	public void dropDatabase() {
		$extravaganza.dropDatabase($pool, $schema);
	}

	@Override
	public void dropAllTablesInDatabase() {
		$extravaganza.dropAllTablesInDatabase($pool);
	}

	@Override
	public void createDatabase() {
		$extravaganza.createDatabase($pool, $schema);
	}

	@Override
	public <T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class, final String $query, final Object... $values) {
		
		try {
			Connection $c = $pool.getConnection();
			try {
				PreparedStatement $stmt = $c.prepareStatement($query,
						ResultSet.TYPE_FORWARD_ONLY);
				for (int $i = 0; $i < $values.length; $i++) {
					$extravaganza.set($stmt, $i + 1, $values[$i]);
				}
				ResultSet $result = $stmt.executeQuery();
				RecordSet<T> $recordSet = new RecordSetImpl<T>(this, $result,
						$schema.getInterface($class));
				$stmt.close();
				return $recordSet;
			} catch (SQLException $exc) {
				throw $exc;
			} catch (IllegalArgumentException $exc) {
				throw new RuntimeException(String.format("The class is %s", $class), $exc);
			} finally {
				$pool.release($c);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

	@Override
	public <T extends ActiveRecord<?>> T selectOne(final Class<T> $class,
			final String $query, final Object... $values) {
		RecordSet<T> $result = select($class, $query, $values);
		if ($result.size() > 0) {
			return $result.get(0);
		}
		return null;
	}

	@Override
	public <T extends ActiveRecord<?>> RecordSet<T> select(final Class<T> $class) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = $extravaganza.makeSelectQuery($sqlName, null, null);
		return select($class, $sqlQuery);
	}

	@Override
	public <T extends ActiveRecord<?>> RecordSet<T> select(
			final Class<T> $class, final int $limit) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = $extravaganza
				.makeSelectQuery($sqlName, null, $limit);
		return select($class, $sqlQuery);
	}

	@Override
	public <T extends ActiveRecord<?>> RecordSet<T> select(
			final Class<T> $class, final int $offset, final int $limit) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = $extravaganza.makeSelectQuery($sqlName, $offset,
				$limit);
		return select($class, $sqlQuery);
	}

	@Override
	public <T extends ActiveRecord<?>> T selectById(final Class<T> $class,
			final int $id) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = $extravaganza.makeSelectQuery($sqlName, $id);
		RecordSet<T> $result = select($class, $sqlQuery);
		if ($result.size() == 1) {
			return $result.get(0);
		}
		return null;
	}

	@Override
	public RecordSet<ActiveRecord<?>> query(final String $query,
			final Object... $values) {
		try {
			Connection $c = $pool.getConnection();
			try {
				PreparedStatement $stmt = $c.prepareStatement($query,
						ResultSet.TYPE_FORWARD_ONLY);
				for (int $i = 0; $i < $values.length; $i++) {
					$extravaganza.set($stmt, $i + 1, $values[$i]);
				}
				ResultSet $result = $stmt.executeQuery();
				ObjectRecordSet $recordSet = new ObjectRecordSet(this, $result);
				$stmt.close();
				return $recordSet;
			} catch (SQLException $exc) {
				throw $exc;
			} finally {
				$pool.release($c);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

	@Override
	public void exec(final String $query, final Object... $values) {
		try {
			Connection $c = $pool.getConnection();
			try {
				PreparedStatement $stmt = $c.prepareStatement($query,
						ResultSet.TYPE_FORWARD_ONLY);
				for (int $i = 0; $i < $values.length; $i++) {
					$extravaganza.set($stmt, $i + 1, $values[$i]);
				}
				$stmt.execute();
				$stmt.close();
			} catch (SQLException $exc) {
				throw $exc;
			} finally {
				$pool.release($c);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

	@Override
	public ActiveRecord<?> querySingle(final String $query,
			final Object... $values) {
		try {
			Connection $c = $pool.getConnection();
			try {
				PreparedStatement $stmt = $c.prepareStatement($query,
						ResultSet.TYPE_FORWARD_ONLY);
				for (int $i = 0; $i < $values.length; $i++) {
					$extravaganza.set($stmt, $i, $values[$i]);
				}
				ResultSet $result = $stmt.executeQuery();
				ObjectRecordSet $records = new ObjectRecordSet(this, $result);
				$stmt.close();
				if ($records.isEmpty()) {
					return null;
				}
				return $records.getFirst();
			} catch (SQLException $exc) {
				throw $exc;
			} finally {
				$pool.release($c);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}
	
	@Override
	public <T extends ActiveRecord<?>> RecordSet<T> selectByExample(final ActiveRecord<T> $example) {
		return selectByExample($example, AND | LIKE);
	}
	
	@Override
	public <T extends ActiveRecord<?>> RecordSet<T> selectByExample(final ActiveRecord<T> $example, final int $options) {
		@SuppressWarnings("unchecked")
		Class<T> $class = (Class<T>) (Proxy.isProxyClass($example.getClass())
			? $example.getClass().getInterfaces()[0]
			: $example.getClass());
		Map<String,Object> $values = $example.newValues();
		if ($values.isEmpty()) {
			try {
				return new RecordSetImpl<T>(this, null, null);
			} catch (SQLException $exc) {
				throw new DatabaseException($exc);
			}
		}
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String[] $where = new String[$values.size()];
		Object[] $objects = new Object[$values.size()];
		int $i = 0;
		for (Entry<String,Object> $value : $values.entrySet()) {
			$where[$i] = getDatabaseExtravaganza().escapeName($value.getKey()) + " <=> ?";
			$objects[$i] = $value.getValue();
			if (($objects[$i] instanceof String) && (($options & LIKE) > 0)) {
				$where[$i] = getDatabaseExtravaganza().escapeName($value.getKey()) + " COLLATE utf8_unicode_ci LIKE ?";
			}
			$i++;
		}
		String $query = "SELECT * FROM " + getDatabaseExtravaganza().escapeName($sqlName)
				+ " WHERE " + AbusingStrings.implode(($options & OR) > 0 ? " OR " : " AND ", $where);
		return select($class, $query, $objects);
	}

	@Override
	public Schema getSchema() {
		return $schema;
	}

	@Override
	public DatabaseExtravaganza getDatabaseExtravaganza() {
		return $extravaganza;
	}

	@Override
	public void close() {
		$pool.close();
	}

}
