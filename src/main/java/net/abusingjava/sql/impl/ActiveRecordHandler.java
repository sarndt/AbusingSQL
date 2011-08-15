package net.abusingjava.sql.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.*;

/**
 * Implements an ActiveRecord at Runtime.
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
	Map<String, Set<ActiveRecord<?>>> $resolvedSets = new HashMap<String, Set<ActiveRecord<?>>>();
	
	Integer $id = null;

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final ResultSet $resultSet) throws SQLException {
		this.$dbAccess = $dbAccess;
		this.$interface = null;
		
		ResultSetMetaData $meta = $resultSet.getMetaData();
		DatabaseExtravaganza $extravaganza = $dbAccess.getDatabaseExtravaganza();
		for (int $i = 1; $i <= $meta.getColumnCount(); $i++) {
			$oldValues.put($meta.getColumnName($i), $extravaganza.get($resultSet, $i, $meta.getColumnType($i)));
		}
	}
	
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
			
		} else if ($methodName == "getPropertyChangeListeners") {
			if ($propertyChangeSupport != null) {
				return $propertyChangeSupport.getPropertyChangeListeners();
			}
			return new PropertyChangeListener[0];
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
				if ($property.isManyToManyPart()) {
					throw new UnsupportedOperationException("ManyToMany-Relationships are currently not resolved.");
				}
				if (!$resolvedSets.containsKey($property.getSqlName())) {
					@SuppressWarnings("unchecked")
					Class<ActiveRecord<?>> $recordType = (Class<ActiveRecord<?>>) $property.getGenericType();
					String $onePart = $property.getOnePart().getSqlName();
					Set<ActiveRecord<?>> $records = $dbAccess.select($recordType,
							"SELECT * FROM " + $dbAccess.getDatabaseExtravaganza().escapeName(
									$dbAccess.getSchema().getInterface($recordType).getSqlName()
									) + " WHERE " + $dbAccess.getDatabaseExtravaganza().escapeName(
											$onePart) + " = " + $id);
					$resolvedSets.put($property.getSqlName(), $records);
					for (ActiveRecord<?> $record : $records) {
						ActiveRecordHandler $handler = (ActiveRecordHandler) Proxy.getInvocationHandler($record);
						$handler.$resolvedRecords.put($onePart, (ActiveRecord<?>) $proxy);
					}
					return $records;
				}
				return $resolvedSets.get($property.getSqlName());
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
			} else if ($property.getGenericType() != null) {
				if ($property.isManyToManyPart()) {
					throw new UnsupportedOperationException("ManyToMany-Relationships are currently not saved :-(");
				}
				throw new UnsupportedOperationException("We’re working on this.");
			}
			Object $oldValue = null;
			if ($newValues.containsKey($propertyName)) {
				$oldValue = $newValues.get($propertyName);
			} else if ($oldValues.containsKey($propertyName)) {
				$oldValue = $oldValues.get($propertyName);
			}
			$newValues.put($propertyName, $args[0]);
			if ($propertyChangeSupport != null) {
				$propertyChangeSupport.firePropertyChange($propertyName, $oldValue, $newValues.get($propertyName));
			}
			
		} else if ($methodName == "equals") {
			if (Proxy.isProxyClass($args[0].getClass())) {
				InvocationHandler $h = Proxy.getInvocationHandler($args[0]);
				if ($h instanceof ActiveRecordHandler) {
					ActiveRecordHandler $r = (ActiveRecordHandler) $h;
					if (!$interface.equals($r.$interface)) {
						return false;
					}
					if (($r.$id != null) || ($id != null)) {
						return $r.$id == $id;
					}
					return $r.$newValues.entrySet().equals($newValues.entrySet());
				}
			}
			
		} else if ($methodName == "hashCode") {
			if ($id != null) {
				return $id;
			}
			return -Math.abs($newValues.hashCode());
			
		} else if ($methodName == "delete") {
			Connection $c = null;
			if ($args.length == 1) {
				$c = (Connection) $args[0];
			}
			if ($c == null) {
				$c = $dbAccess.getConnection();
			}
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
			Connection $c = null;
			int $depth = 0;
			if ($args.length == 1) {
				if ($args[0] instanceof Integer) {
					$depth = (Integer) $args[0];
				} else {
					$c = (Connection) $args[0];
				}
			} else if ($args.length == 2) {
				$c = (Connection) $args[0];
				$depth = (Integer) $args[1];
			}
			if ($c == null) {
				$c = $dbAccess.getConnection();
			}
			boolean $commit = false;
			if ($depth > 0) {
				if ($c.getAutoCommit()) {
					$commit = true;
					$c.setAutoCommit(false);
				}
				for (Entry<String,ActiveRecord<?>> $e : $resolvedRecords.entrySet()) {
					ActiveRecord<?> $o = $e.getValue();
					if (($o != null) && $o.hasChanges()) {
						$o.saveChanges($c, $depth - 1);
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
			
			if ($commit) {
				try {
					$c.commit();
				} catch (SQLException $exc) {
					throw new DatabaseException($exc);
				} finally {
					$c.setAutoCommit(true);
				}
			}
		} else if ($methodName == "toString") {
			return $interface.getName() + '#' + $id;
			
		} else if ($methodName == "hasChanges") {
			return !$newValues.isEmpty();
			
		} else if ($methodName == "clearCache") {
			$resolvedRecords.clear();
			$resolvedSets.clear();
			
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
			
		}
		return $proxy;
	}
}
