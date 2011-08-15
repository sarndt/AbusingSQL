package net.abusingjava.sql.impl;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.*;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class DatabaseAccessImpl implements DatabaseAccess {

	final ConnectionPool $pool;
	final Schema $schema;
	final DatabaseExtravaganza $extravaganza;
	
	Connection $connection = null;
	
	DatabaseAccessImpl(final DatabaseExtravaganza $extravaganza, final ConnectionPool $pool, final Schema $schema) {
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
		T $instance = (T) Proxy.newProxyInstance($class.getClassLoader(), new Class<?>[] { $class },
				new ActiveRecordHandler(this, $schema.getInterface($class)));
		return $instance;
	}
	
	@Override
	public void dropDatabase() {
		$extravaganza.dropDatabase($pool, $schema);
	}

	@Override
	public void createDatabase() {
		$extravaganza.createDatabase($pool, $schema);
	}
	
	@Override
	public <T extends ActiveRecord<?>> RecordSetImpl<T> select(final Class<T> $class, final String $query, final Object... $values) {
		try {
			Connection $c = $pool.getConnection();
			try {
				PreparedStatement $stmt = $c.prepareStatement($query);
				for (int $i = 0; $i < $values.length; $i++) {
					// TODO FIXME FIXME FIXME FIXME FIXME
					// ActiveRecordHandler.setValue($stmt, $i+1, $values[$i]);
				}
				ResultSet $result = $stmt.executeQuery();
				return new RecordSetImpl<T>(this, $result, $schema.getInterface($class));
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
	public <T extends ActiveRecord<?>> T selectOne(final Class<T> $class, final String $query, final Object... $values) {
		RecordSetImpl<T> $result = select($class, $query, $values);
		if ($result.size() > 0) {
			return $result.get(0);
		}
		return null;
	}
	
	@Override
	public <T extends ActiveRecord<?>> RecordSetImpl<T> select(final Class<T> $class, final Class<?>... $joinClasses) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = "SELECT * FROM `" + $sqlName + "`";
		return select($class, $sqlQuery);
	}

	@Override
	public <T extends ActiveRecord<?>> RecordSetImpl<T> select(final Class<T> $class, final int $limit, final Class<?>... $joinClasses) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = "SELECT * FROM `" + $sqlName + "` LIMIT " + $limit;
		return select($class, $sqlQuery);
	}

	@Override
	public <T extends ActiveRecord<?>> RecordSetImpl<T> select(final Class<T> $class, final int $offset, final int $limit, final Class<?>... $joinClasses) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = "SELECT * FROM `" + $sqlName + "` LIMIT " + $limit + " OFFSET " + $offset;
		return select($class, $sqlQuery);
	}

	@Override
	public <T extends ActiveRecord<?>> T selectById(final Class<T> $class, final int $id) {
		String $sqlName = DatabaseSQL.makeSQLName($class.getSimpleName());
		String $sqlQuery = "SELECT * FROM `" + $sqlName + "` WHERE `id` = " + $id;
		RecordSetImpl<T> $result = select($class, $sqlQuery);
		if ($result.size() == 1) {
			return $result.getFirst();
		}
		return null;
	}

	@Override
	public RecordSetImpl<ActiveRecord<?>> query(final String $query, final Object... $values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beginTransaction() {
		// TODO: Transaction Management implementieren
	}

	@Override
	public void rollbackTransaction() {
		
	}
	
	@Override
	public void abortTransaction() {
		
	}
	
	@Override
	public void commitTransaction() {
		
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