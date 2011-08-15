package net.abusingjava.sql.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.*;

/**
 * Implementiert einen ActiveRecord zur Runtime.
 */
@Author("Julian Fleischer")
@Version("2011-08-15")
public class ActiveRecordHandler implements InvocationHandler {

	final DatabaseAccess $dbAccess;
	final Interface $interface;
	PropertyChangeSupport $propertyChangeSupport = null;
	Map<String, Object> $oldValues = new HashMap<String, Object>();
	Map<String, Object> $newValues = new HashMap<String, Object>();
	Map<String, ActiveRecord<?>> $resolvedRecords = new HashMap<String, ActiveRecord<?>>();
	
	Integer $id = null;

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Interface $interface, final ResultSet $resultSet) throws SQLException {
		this.$dbAccess = $dbAccess;
		this.$interface = $interface;
		
		for (Property $p : $interface.getProperties()) {
			try {
				$oldValues.put($p.getSqlName(),
					$dbAccess.getDatabaseExtravaganza().get($resultSet, $p.getSqlName(), $p.getJavaType()));
			} catch (SQLException $exc) {
			}
		}
		$id = $resultSet.getInt("id");
	}

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Interface $interface) {
		this.$dbAccess = $dbAccess;
		this.$interface = $interface;
	}
	
	@Override
	public Object invoke(final Object $proxy, final Method $method, Object[] $args) throws Throwable {

		if ($args == null) {
			$args = new Object[] {};
		}
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
			$propertyName = DatabaseSQL.makeSQLName($propertyName);
			
			if ($property.isManyPart()) {
				if ($resolvedRecords.containsKey($propertyName)) {
					return $resolvedRecords.get($propertyName);
				}
				Integer $id = (Integer) ($newValues.containsKey($propertyName)
						? $newValues.get($propertyName)
						: ($oldValues.containsKey($propertyName)
								? $oldValues.get($propertyName)
								: null));
				if ($id == null) {
					return null;
				}
				@SuppressWarnings("unchecked")
				ActiveRecord<?> $record = $dbAccess.selectById((Class<? extends ActiveRecord<?>>) $property.getJavaType(), (int) $id);
				$resolvedRecords.put($propertyName, $record);
				return $record;
				
			} else if ($property.getGenericType() != null) {
				
			}
			if ($newValues.containsKey($propertyName)) {
				return $newValues.get($propertyName);
			} else if ($oldValues.containsKey($propertyName)) {
				return $oldValues.get($propertyName);
			}
			
		} else if ($methodName.startsWith("set")) {
			String $propertyName = $methodName.substring(3);
			Property $property = $interface.getProperty($propertyName);
			$propertyName = DatabaseSQL.makeSQLName($propertyName);
			if ($args[0] instanceof ActiveRecord) {
				$resolvedRecords.put($propertyName, (ActiveRecord<?>) $args[0]);
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
			try {
				$dbAccess.getDatabaseExtravaganza().doDelete($c, $interface.getSqlName(), $id);
				$id = null;
			} catch (SQLException $exc) {
				throw new DatabaseException($exc);
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
			Connection $c = $dbAccess.getConnection();
			int $depth = 0;
			if ($args.length == 1) {
				$depth = (Integer) $args[0];
			}
			if ($depth > 0) {
				// BEGIN TRANSACTION
				for (Entry<String,ActiveRecord<?>> $e : $resolvedRecords.entrySet()) {
					ActiveRecord<?> $o = $e.getValue();
					if (($o != null) && $o.hasChanges()) {
						$o.saveChanges($depth - 1);
						$newValues.put($e.getKey(), $o.getId());
					}
				}
			}
			String[] $properties = $newValues.keySet().toArray(new String[$newValues.keySet().size()]);
			Object[] $values = new Object[$properties.length];
			int $i = 0;
			for (String $p : $properties) {
				$values[$i] = $newValues.get($p);
				$i++;
			}
			try {
				if ($id == null) {
					$id = $dbAccess.getDatabaseExtravaganza().doInsert($c, $interface.getSqlName(), $properties, $values);
				} else {
					if (!$newValues.isEmpty()) {
						$dbAccess.getDatabaseExtravaganza().doUpdate($c, $interface.getSqlName(), $properties, $values, $id);
					}
				}
			} catch (SQLException $exc) {
				throw new DatabaseException($exc);
			} finally {
				$dbAccess.release($c);
			}
			for (Entry<String,Object> $e : $oldValues.entrySet()) {
				if (!$newValues.containsKey($e.getKey())) {
					$newValues.put($e.getKey(), $e.getValue());
				}
			}
			$oldValues = $newValues;
			$newValues = new HashMap<String,Object>();
			
			if ($args.length == 1) {
				// COMMIT TRANSACTION
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
		return $proxy;
	}
}
