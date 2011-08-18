package net.abusingjava.sql.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.abusingjava.Author;
import net.abusingjava.Experimental;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.sql.ConnectionProvider;
import net.abusingjava.sql.DatabaseException;
import net.abusingjava.sql.Property;
import net.abusingjava.sql.Schema;

@Author("Julian Fleischer")
@Version("2011-08-18")
@Since("1.0")
@Experimental
public class DatabasePostgreSQL extends AbstractDatabaseExtravaganza {

	@Override
	public String escapeName(final String $name) {
		return '"' + $name + '"';
	}

	@Override
	public String getDriverName() {
		return "org.postgresql.Driver";
	}

	@Override
	public void createDatabase(final ConnectionProvider $pool, final Schema $schema) {
		
	}
	
	@Override
	public void dropDatabase(final ConnectionProvider $pool, final Schema $schema) {
		
	}

	@Override
	public String getSqlType(final Property $property) {
		return null;
	}

	@Override
	public int doInsert(final Connection $c, final String $table, final String[] $properties, final Object[] $values) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void doUpdate(final Connection $c, final String $table, final String[] $properties, final Object[] $values, final int $id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDelete(final Connection $c, final String $table, final int $id) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object get(final ResultSet $resultSet, final String $column, final Class<?> $javaType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(final PreparedStatement $stmt, final int $index, final Object $value) {
		try {
			if ($value instanceof Integer) {
				$stmt.setInt($index, (Integer)$value);
			} else if ($value instanceof String) {
				$stmt.setString($index, (String)$value);
			} else if ($value instanceof Boolean) {
				$stmt.setBoolean($index,  (Boolean)$value);
			} else if ($value instanceof Enum) {
				$stmt.setString($index, ((Enum<?>)$value).name());
			} else if ($value instanceof byte[]) {
				$stmt.setBytes($index, (byte[])$value);
			} else if ($value instanceof Date) {
				$stmt.setTimestamp($index, new java.sql.Timestamp( ((Date)$value).getTime() ));
			} else if ($value instanceof Float) {
				$stmt.setFloat($index, (Float)$value);
			} else if ($value instanceof Double) {
				$stmt.setDouble($index, (Double)$value);
			} else if ($value instanceof BigDecimal) {
				$stmt.setBigDecimal($index, (BigDecimal)$value);
			} else if ($value instanceof Short) {
				$stmt.setShort($index, (Short)$value);
			} else if ($value instanceof Byte) {
				$stmt.setByte($index, (Byte)$value);
			} else if ($value == null) {
				$stmt.setNull($index, 0);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}
}
