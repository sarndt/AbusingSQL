package net.abusingjava.sql.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.*;
import net.abusingjava.strings.AbusingStrings;

/**
 * Implementiert einen ActiveRecord zur Runtime.
 */
@Author("Julian Fleischer")
@Version("2011-08-13")
public class ActiveRecordHandler implements InvocationHandler {

	final DatabaseAccess $dbAccess;
	final Interface $interface;
	PropertyChangeSupport $propertyChangeSupport = null;
	Map<String, Object> $oldValues = new HashMap<String, Object>();
	Map<String, Object> $newValues = new HashMap<String, Object>();
	
	Integer $id = null;

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Interface $interface, final ResultSet $resultSet) throws SQLException {
		this.$dbAccess = $dbAccess;
		this.$interface = $interface;
		
		while ($resultSet.next()) {
			
		}
	}

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Interface $interface) {
		this.$dbAccess = $dbAccess;
		this.$interface = $interface;
	}
	
	@Override
	public Object invoke(final Object $proxy, final Method $method, final Object[] $args) throws Throwable {

		String $methodName = $method.getName().intern();

		
		if ($methodName == "getId") {
			return $id;
			
		} else if ($methodName == "get") {
			if ($newValues.containsKey($args[0])) {
				return $newValues.get($args[0]);
			}
			return $oldValues.get($args[0]);
			
		} else if ($methodName == "set") {
			String $propertyName = (String) $args[0];
			if ($propertyChangeSupport == null) {

				$newValues.put($propertyName, $args[1]);	
			} else {
				Object $oldValue = null;
				if ($newValues.containsKey($propertyName)) {
					$oldValue = $newValues.get($propertyName);
				} else if ($oldValues.containsKey($propertyName)) {
					$oldValue = $oldValues.get($propertyName);
				}
				$newValues.put($propertyName, $args[1]);
				$propertyChangeSupport.firePropertyChange($propertyName, $oldValue, $args[1]);
			}
			
		} else if ($methodName.startsWith("get")) {
			String $propertyName = $methodName.substring(3);
			Property $property = $interface.getProperty($propertyName);
			if ($newValues.containsKey($propertyName)) {
				return $newValues.get($propertyName);
			} else if ($oldValues.containsKey($propertyName)) {
				return $oldValues.get($propertyName);
			}
			
		} else if ($methodName.startsWith("set")) {
			String $propertyName = $methodName.substring(3);
			Property $property = $interface.getProperty($propertyName);
			if ($args[0] instanceof ActiveRecord) {
				$args[0] = ((ActiveRecord<?>)$args[0]).getId();
			}
			$newValues.put($property.getSqlName(), $args[0]);
			if ($propertyChangeSupport != null) {
				$propertyChangeSupport.firePropertyChange($propertyName,
						$oldValues.get($property.getSqlName()),
						$newValues.get($property.getSqlName()));
			}
			
		} else if ($methodName == "equals") {
			// TODO: equals
			
		} else if ($methodName == "hashCode") {
			// TODO: hashCode
			
		} else if ($methodName == "delete") {
			Connection $c = $dbAccess.getConnection();
			String $query = $dbAccess.getDatabaseExtravaganza().getDeleteQuery($interface, $id);
			try {
				$c.createStatement().executeUpdate($query);
				$id = null;
			} catch (SQLException $exc) {
				throw $exc;
			} finally {
				$dbAccess.release($c);
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
			
			if ($args.length == 1) {
				if ($id == null) {
					// insert
				} else {
					// update
				}
			} else if ($args.length < 1) {
				if ($id == null) {
					// insert
				} else {
					// update
				}
				$oldValues = $newValues;
				$newValues = new HashMap<String,Object>();
			}
			
		} else if ($methodName == "toString") {
			return $interface.getName() + '#' + $id;
			
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
		//return $proxy;
		
		
		if ($methodName == "saveChanges") {
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
					String $query = "INSERT INTO `" + $interface.getSqlName() + "` ("
							+ $columns + ") VALUES (" + $values + ")";
					PreparedStatement $stmt = $c.prepareStatement($query, Statement.RETURN_GENERATED_KEYS);
					// prepare($stmt, $newValues);
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
					String $query = "UPDATE `" + $interface.getSqlName() + "` SET "
							+ AbusingStrings.implode(",", $properties) + " WHERE `id` = ?";
					PreparedStatement $stmt = $c.prepareStatement($query);
					// prepare($stmt, $newValues);
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
		}
		return $proxy;
	}

}
