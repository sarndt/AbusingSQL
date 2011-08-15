package net.abusingjava.sql.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.DatabaseException;
import net.abusingjava.sql.DatabaseExtravaganza;

@Author("Julian Fleischer")
@Version("2011-08-15")
abstract class AbstractDatabaseExtravaganza implements DatabaseExtravaganza {
	
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
				$stmt.setTimestamp($index, new Timestamp(((Date)$value).getTime()));
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
	
	protected void prepare(final PreparedStatement $stmt, final Object[] $values) {
		int $i = 0;
		for (Object $o : $values) {
			set($stmt, ++$i, $o);
		}
	}

	@Override
	public String makeSelectQuery(final String $table, final Integer $offset, final Integer $limit) {
		return "SELECT * FROM " + escapeName($table) + ($limit == null ? "" : " LIMIT " + $limit + ($offset == null ? "" : " OFFSET " + $limit));
	}

	@Override
	public String makeSelectQuery(final String $table, final int $id) {
		return "SELECT * FROM " + escapeName($table) + "WHERE " + escapeName("id") + " = " + $id;
	}
	

	@Override
	public Object get(final ResultSet $resultSet, final int $i, final int $columnType) {
		try {
			Object $value = null;
			switch ($columnType) {
			case Types.INTEGER:
			case Types.SMALLINT:
				$value = $resultSet.getInt($i);
				break;
			case Types.BOOLEAN:
			case Types.BIT:
				$value = $resultSet.getBoolean($i);
				break;
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
				$value = $resultSet.getString($i);
				break;
			case Types.TIMESTAMP:
				$value = $resultSet.getTimestamp($i);
				$value = new Date(((Timestamp) $value).getTime());
				break;
			case Types.BIGINT:
				$value = $resultSet.getLong($i);
				break;
			case Types.VARBINARY:
				$value = $resultSet.getBytes($i);
				break;
			case Types.FLOAT:
				$value = $resultSet.getFloat($i);
				break;
			case Types.REAL:
				$value = $resultSet.getDouble($i);
				break;
			case Types.DATE:
				$value = $resultSet.getDate($i);
				break;
			case Types.TIME:
				$value = $resultSet.getTime($i);
				break;
			}
			if ($resultSet.wasNull()) {
				$value = null;
			}
			return $value;
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

}
