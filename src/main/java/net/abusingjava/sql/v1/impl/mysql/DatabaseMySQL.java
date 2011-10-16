package net.abusingjava.sql.v1.impl.mysql;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.EnumSet;

import net.abusingjava.AbusingStrings;
import net.abusingjava.sql.DatabaseException;
import net.abusingjava.sql.v1.ActiveRecord;
import net.abusingjava.sql.v1.DatabaseAccess;
import net.abusingjava.sql.v1.DatabaseExtravaganza;

public class DatabaseMySQL implements DatabaseExtravaganza {

	@Override
	public void createTables(final DatabaseAccess $databaseAccess) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSchema(final DatabaseAccess $databaseAccess) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropSchema(final DatabaseAccess $databaseAccess) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropTables(final DatabaseAccess $databaseAccess, final Class<?>[] $tables) {
		// TODO Auto-generated method stub

	}

	@Override
	public void flushTables(final DatabaseAccess $databaseAccess, final Class<?>[] $tables) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends ActiveRecord<T>> String getSelectQuery(final Class<T> $class, final int $offset, final int $limit,
			final Class<?>... $joinClasses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(final PreparedStatement $stmt, final int $index, final Object $value) {
		try {
			if ($value instanceof Integer) {
				$stmt.setInt($index, (Integer)$value);
			} else if ($value instanceof String) {
				$stmt.setString($index, (String)$value);
			} else if ($value instanceof Boolean) {
				$stmt.setBoolean($index,  (Boolean)$value);
			} else if ($value instanceof EnumSet) {
				EnumSet<?> $enumSet = (EnumSet<?>) $value;
				String[] $names = new String[$enumSet.size()];
				int $i = 0;
				for (Enum<?> $e : $enumSet) {
					$names[$i] = $e.name();
					$i++;
				}
				$stmt.setString($index, AbusingStrings.implode(",", $names));
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

	@Override
	public <T extends ActiveRecord<T>> String getSelectQuery(final T $example, final int $offset, final int $limit,
			final Class<?>... $joinClasses) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ActiveRecord<T>> Object[] getExampleValues(final T $example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ActiveRecord<T>> String getSelectByIdQuery(final Class<T> $class) {
		// TODO Auto-generated method stub
		return null;
	}
}
