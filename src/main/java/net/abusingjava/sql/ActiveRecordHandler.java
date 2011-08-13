package net.abusingjava.sql;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.strings.AbusingStrings;

import com.mysql.jdbc.Statement;

/**
 * Implementiert einen ActiveRecord zur Runtime.
 */
@Author("Julian Fleischer")
@Version("2011-08-10")
public class ActiveRecordHandler implements InvocationHandler {

	final DatabaseAccess $dbAccess;
	final Class<? extends ActiveRecord<?>> $class;
	PropertyChangeSupport $propertyChangeSupport = null;
	Map<String, Object> $oldValues = new HashMap<String, Object>();
	Map<String, Object> $newValues = new HashMap<String, Object>();
	
	Integer $id = null;

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Class<? extends ActiveRecord<?>> $class, final ResultSet $resultSet) throws SQLException {
		this.$dbAccess = $dbAccess;
		this.$class = $class;
		
		this.$id = $resultSet.getInt("id");
		
		ResultSetMetaData $meta = $resultSet.getMetaData();
		for (int $i = 1; $i <= $meta.getColumnCount(); $i++) {
			int $type = $meta.getColumnType($i);
			String $name = $meta.getColumnName($i);
			
			switch ($type) {
			case Types.INTEGER:
				Integer $int = $resultSet.getInt($i);
				$oldValues.put($name, $resultSet.wasNull() ? null : $int);
				break;
			case Types.BOOLEAN:
				Boolean $boolean = $resultSet.getBoolean($i);
				$oldValues.put($name, $resultSet.wasNull() ? null : $boolean);
				break;
			case Types.VARCHAR:
				String $string = $resultSet.getString($i);
				$oldValues.put($name, $resultSet.wasNull() ? null : $string);
				break;
			case Types.VARBINARY:
				byte[] $byteArray = $resultSet.getBytes($i);
				$oldValues.put($name,  $resultSet.wasNull() ? null : $byteArray);
				break;
			case Types.DATE:
				Date $date = $resultSet.getDate($i);
				$oldValues.put($name, $resultSet.wasNull() ? null : $date);
				break;
			case Types.TIMESTAMP:
				Timestamp $timestamp = $resultSet.getTimestamp($i);
				$oldValues.put($name, $resultSet.wasNull() ? null : new Date($timestamp.getTime()));
				break;
			case Types.LONGVARCHAR:
				String $text = $resultSet.getString($i);
				$oldValues.put($name, $resultSet.wasNull() ? null : $text);
				break;
			}
		}
	}

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Class<? extends ActiveRecord<?>> $class) {
		this.$dbAccess = $dbAccess;
		this.$class = $class;
	}

	private static void prepare(final PreparedStatement $stmt, final Map<String, Object> $values) throws SQLException {
		int $i = 0;
		for (String $key : $values.keySet()) {
			$i++;
			Object $o = $values.get($key);
			setValue($stmt, $i, $o);
		}
	}
	
	static void setValue(final PreparedStatement $stmt, final int $i, final Object $o) throws SQLException {
		if ($o instanceof Integer) {
			$stmt.setInt($i, (Integer) $o);
		} else if ($o instanceof String) {
			$stmt.setString($i, (String) $o);
		} else if ($o instanceof Enum) {
			$stmt.setString($i, ((Enum<?>)$o).name());
		} else if ($o instanceof Boolean) {
			$stmt.setBoolean($i, (Boolean)$o);
		} else if ($o instanceof Date) {
			$stmt.setTimestamp($i, new java.sql.Timestamp(((Date) $o).getTime()));
		} else if ($o instanceof byte[]) {
			$stmt.setBytes($i, (byte[])$o);
		} else if ($o instanceof ActiveRecord) {
			ActiveRecord<?> $r = (ActiveRecord<?>) $o;
			if ($r.exists()) {
				$stmt.setInt($i, $r.getId());
			}
		}
	}
	
	@Override
	public Object invoke(final Object $proxy, final Method $method, final Object[] $args) throws Throwable {

		String $methodName = $method.getName().intern();

		if ($methodName == "getId") {
			return $id;
		} else if ($methodName == "get") {
			return $newValues.get($args[0]);
		} else if ($methodName == "set") {
			$newValues.put((String) $args[0], $args[1]);
		} else if ($methodName.startsWith("get")) {
			String $propertyName = DatabaseSQL.makeSQLName($methodName.substring(3));
			if ($newValues.containsKey($propertyName)) {
				return $newValues.get($propertyName);
			} else if ($oldValues.containsKey($propertyName)) {
				return $oldValues.get($propertyName);
			}
			return null;
		} else if ($methodName.startsWith("set")) {
			String $propertyName = DatabaseSQL.makeSQLName($methodName.substring(3));
			$newValues.put($propertyName, $args[0]);
		} else if ($methodName == "toString") {
			return $class.getSimpleName() + "#" + $id;
		} else if ($methodName == "delete") {
			if ($id != null) {
				// DELETE
				Connection $c = $dbAccess.getConnection();
				String $query = "DELETE FROM " + DatabaseSQL.makeSQLName($class.getSimpleName())
						+ " WHERE `id` = " + $id;
				try {
					$c.createStatement().executeUpdate($query);
					$id = null;
				} catch (SQLException $exc) {
					throw $exc;
				} finally {
					$dbAccess.release($c);
				}
			}
		} else if ($methodName == "saveChanges") {
			if ($proxy instanceof Entity) {
				Date $now = new Date(System.currentTimeMillis());
				if ($id == null) {
					$newValues.put("created", $now);
				} else {
					$newValues.remove("created");
				}
				$newValues.put("last_modified", $now);
			}
			
			String[] $properties = $newValues.keySet().toArray(new String[$newValues.keySet().size()]);
			for (int $i = 0; $i < $properties.length; $i++) {
				$properties[$i] = "`" + $properties[$i] + "`";
			}
			if ($id == null) {
				// INSERT
				Connection $c = $dbAccess.getConnection();
				try {
					String $columns = AbusingStrings.implode(",", $properties);
					for (int $i = 0; $i < $properties.length; $i++) {
						$properties[$i] = "?";
					}
					String $values = AbusingStrings.implode(",", $properties);
					String $query = "INSERT INTO `" + DatabaseSQL.makeSQLName($class.getSimpleName()) + "` ("
							+ $columns + ") VALUES (" + $values + ")";
					PreparedStatement $stmt = $c.prepareStatement($query, Statement.RETURN_GENERATED_KEYS);
					prepare($stmt, $newValues);
					$stmt.executeUpdate();
					ResultSet $key = $stmt.getGeneratedKeys();
					$key.next();
					$id = $key.getInt(1);
				} catch (SQLException $exc) {
					throw $exc;
				} finally {
					$dbAccess.release($c);
				}
			} else {
				// UPDATE
				Connection $c = $dbAccess.getConnection();
				try {
					for (int $i = 0; $i < $properties.length; $i++) {
						$properties[$i] += " = ?";
					}
					String $query = "UPDATE `" + DatabaseSQL.makeSQLName($class.getSimpleName()) + "` SET "
							+ AbusingStrings.implode(",", $properties) + " WHERE `id` = ?";
					PreparedStatement $stmt = $c.prepareStatement($query);
					prepare($stmt, $newValues);
					$stmt.setInt($properties.length + 1, $id);
					$stmt.executeUpdate();
				} catch (SQLException $exc) {
					throw $exc;
				} finally {
					$dbAccess.release($c);
				}
			}
			$oldValues = $newValues;
			$newValues = new HashMap<String,Object>();
		} else if ($methodName == "hasChanges") {
			return !$newValues.isEmpty();
		} else if ($methodName == "discardChanges") {
			$newValues.clear();
		} else if ($methodName == "exists") {
			return $id != null;
		} else if ($methodName == "addPropertyChangeListener") {
			if ($propertyChangeSupport == null) {
				$propertyChangeSupport = new PropertyChangeSupport($proxy);
			}
			$propertyChangeSupport.addPropertyChangeListener((PropertyChangeListener) $args[0]);
		} else if ($methodName == "removePropertyChangeListener") {
			if ($propertyChangeSupport != null) {
				$propertyChangeSupport.removePropertyChangeListener((PropertyChangeListener) $args[0]);
			}
		} else if ($methodName == "getPropertyChangeListeners") {
			if ($propertyChangeSupport != null) {
				return $propertyChangeSupport.getPropertyChangeListeners();
			}
			return new PropertyChangeListener[0];
		}
		return $proxy;
	}

}
