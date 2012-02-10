package net.abusingjava.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import net.abusingjava.AbusingStrings;
import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.sql.impl.GenericDatabaseAccessFactory;

@Author("Julian Fleischer")
@Version("2011-08-16")
@Since(version = "1.0", value = "2011-08-16")
final public class AbusingSQL {

	AbusingSQL() {
	}

	public static DatabaseAccessFactory newDatabaseAccessFactory(final String $name) {
		try {
			return (DatabaseAccessFactory) Class.forName($name).newInstance();
		} catch (Exception $exc) {
			throw new RuntimeException($exc);
		}
	}

	public static <T extends DatabaseAccessFactory> T newDatabaseAccessFactory(final Class<T> $class) {
		try {
			return $class.newInstance();
		} catch (Exception $exc) {
			throw new RuntimeException($exc);
		}
	}

	public static DatabaseAccessFactory newDatabaseAccessFactory(final DatabaseExtravaganza $extravaganza,
			final Class<?>... $classes) {
		return new GenericDatabaseAccessFactory($extravaganza, $classes);
	}

	private static String debugValue(final Object $value) {
		if ($value instanceof Number) {
			return $value.toString();
		} else if ($value instanceof String) {
			return '"' + $value.toString() + '"';
		}
		return $value.toString();
	}

	/**
	 * Shows a prepared query with values filled in.
	 * <p>
	 * 
	 * @param $query
	 *            A query containing place holders (“?”) like in a
	 *            {@link java.sql.PreparedStatement}.
	 * @param $values
	 *            The values to be filled in. Must be exactly the same number of
	 *            values as place-holders in $query.
	 * @return The final query with values filled in.
	 */
	public static String debugQuery(final String $query, final Object... $values) {
		int $offset = 0;
		String[] $pieces = new String[($values.length * 2) + 1];
		try {
			for (int $i = 0; ($i < $values.length); $i++) {
				int $index = $query.indexOf('?', $offset);
				$pieces[$i * 2] = $query.substring($offset, $index);
				$pieces[($i * 2) + 1] = debugValue($values[$i]);
				$offset = $index + 1;
			}
			$pieces[$pieces.length - 1] = $query.substring($offset);
		} catch (Exception $exc) {
			
		}
		return AbusingStrings.implode("", $pieces);
	}

	/**
	 * Translates a ResultSet into a Map.
	 */
	public static Map<String, Object> loadResultSet(final ResultSet $result) throws SQLException {
		Map<String, Object> $map = new HashMap<String, Object>();

		ResultSetMetaData $meta = $result.getMetaData();
		int $count = $meta.getColumnCount();
		for (int $i = 1; $i <= $count; $i++) {
			String $key = $meta.getColumnLabel($i);
			Object $value = null;
			switch ($meta.getColumnType($i)) {
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
				$value = $result.getInt($i);
				break;
			case Types.FLOAT:
				$value = $result.getFloat($i);
				break;
			case Types.DOUBLE:
				$value = $result.getDouble($i);
				break;
			case Types.BIT:
			case Types.BOOLEAN:
				$value = $result.getBoolean($i);
				break;
			case Types.BIGINT:
				$value = $result.getLong($i);
				break;
			case Types.DECIMAL:
				$value = $result.getBigDecimal($i);
				break;
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				$value = $result.getString($i);
				break;
			case Types.TIME:
				$value = $result.getDate($i);
				break;
			case Types.TIMESTAMP:
				$value = $result.getDate($i);
				break;
			case Types.DATE:
				$value = $result.getDate($i);
				break;
			}
			$map.put($key, $value);
		}
		return $map;
	}
}
