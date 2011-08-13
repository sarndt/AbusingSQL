package net.abusingjava.sql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

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
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}
	
}
