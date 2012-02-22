package net.abusingjava.sql.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.DatabaseException;
import net.abusingjava.sql.DatabaseExtravaganza;

@Author("Julian Fleischer")
@Version("2011-08-15")
abstract class AbstractDatabaseExtravaganza implements DatabaseExtravaganza {
	
	
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
		return "SELECT * FROM " + escapeName($table) + " WHERE " + escapeName("id") + " = " + $id;
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
			case Types.TINYINT:
				$value = $resultSet.getBoolean($i);
				break;
			case Types.CHAR:
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
			case Types.NCHAR:
				$value = $resultSet.getString($i);
				break;
			case Types.TIMESTAMP:
				$value = $resultSet.getTimestamp($i);
				try {
					$value = new Date(((Timestamp) $value).getTime());
				} catch (Exception $exc) {
					$value = null;
				}
				break;
			case Types.BIGINT:
				$value = $resultSet.getLong($i);
				break;
			case Types.VARBINARY:
			case Types.BLOB:
			case Types.LONGVARBINARY:
				$value = $resultSet.getBytes($i);
				break;
			case Types.FLOAT:
				$value = $resultSet.getFloat($i);
				break;
			case Types.DOUBLE:
			case Types.REAL:
				$value = $resultSet.getDouble($i);
				break;
			case Types.DATE:
				$value = $resultSet.getDate($i);
				break;
			case Types.TIME:
				$value = $resultSet.getTime($i);
				break;
			case Types.DECIMAL:
				$value = $resultSet.getBigDecimal($i);
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
