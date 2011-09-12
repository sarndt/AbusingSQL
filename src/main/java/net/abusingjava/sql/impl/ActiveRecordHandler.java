package net.abusingjava.sql.impl;

import static net.abusingjava.functions.AbusingFunctions.callback;

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

import net.abusingjava.AbusingStrings;
import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.functions.AbusingFunctions;
import net.abusingjava.sql.*;
import net.abusingjava.sql.schema.Entity;
import net.abusingjava.sql.schema.Interface;
import net.abusingjava.sql.schema.ManyToMany;
import net.abusingjava.sql.schema.Property;

/**
 * Implements an ActiveRecord at Runtime.
 */
@Author("Julian Fleischer")
@Version("2011-09-09")
public class ActiveRecordHandler implements InvocationHandler {

	private final DatabaseAccess $dbAccess;
	private final Interface $interface;
	private PropertyChangeSupport $propertyChangeSupport = null;
	private Map<String, Object> $oldValues = new HashMap<String, Object>();
	private Map<String, Object> $newValues = new HashMap<String, Object>();
	private final Map<String, ActiveRecord<?>> $resolvedRecords = new HashMap<String, ActiveRecord<?>>();
	private final Map<String, SetList<ActiveRecord<?>>> $resolvedSets = new HashMap<String, SetList<ActiveRecord<?>>>();
	
	private Integer $id = null;

	
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
				if ($p.getEnumType() != null) {
					String $string = $resultSet.getString($p.getSqlName());
					if ($string == null) {
						$string = "";
					}
					$oldValues.put($p.getSqlName(),
						AbusingFunctions.callback(this, "makeEnumSet").call($p.getEnumType(), $string));
				} else {
					$oldValues.put($p.getSqlName(),
						$dbAccess.getDatabaseExtravaganza().get($resultSet, $p.getSqlName(), $p.getJavaType()));
				}
			} catch (SQLException $exc) {
			}
		}
		$id = $resultSet.getInt("id");
	}

	ActiveRecordHandler(final DatabaseAccess $dbAccess, final Interface $interface) {
		this.$dbAccess = $dbAccess;
		this.$interface = $interface;

		for (Property $p : $interface.getProperties()) {
			if ($p.getEnumType() != null) {
				$oldValues.put($p.getSqlName(),
						AbusingFunctions.callback(this, "makeEnumSet").call($p.getEnumType(), ""));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> EnumSet<E> makeEnumSet(final Class<E> $enumType, final String $values) {
		String[] $parts = (($values == null) || $values.isEmpty()) ? new String[0] : $values.split(",");
		EnumSet<E> $set = EnumSet.noneOf($enumType);
		for (String $part : $parts) {
			$set.add((E) callback(Enum.class, "valueOf").call($enumType, $part));
		}
		return $set;
	}

	@Override
	public Object invoke(final Object $proxy, final Method $method, Object[] $args) throws Throwable {

		if ($args == null) {
			$args = new Object[] {};
		}
		String $methodName = $method.getName().intern();
		
		if (($interface != null) && $interface.hasHandler($method)) {
			Mixin<? extends ActiveRecord<?>> $handler = $interface.getHandler($method).newInstance();
			$handler.getClass().getMethod("setActiveRecord", ActiveRecord.class).invoke($handler, $proxy);
			$handler.setDatabaseAccess($dbAccess);
			Object $result = $handler.getClass()
				.getMethod($method.getName(), $method.getParameterTypes())
				.invoke($handler, $args);
			return $result == null ? $proxy : $result;
		}
		
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
				if (!$resolvedSets.containsKey($property.getSqlName())) {
					if ($id == null) {
						SetList<ActiveRecord<?>> $record = new RecordSetImpl<ActiveRecord<?>>($dbAccess, null, $property.getParent());
						$resolvedSets.put($property.getSqlName(), $record);
						return $record;
					}
					if ($property.isManyToManyPart()) {
						for (ManyToMany $m : $property.getManyToManyRelationships()) {
							if (($m.getFrom().getJavaType() == $property.getGenericType())
									|| ($m.getTo().getJavaType() == $property.getGenericType())) {
								String $otherTable = $m.getTheOther($property).getParent().getSqlName();
								String $relationTable = $m.getFrom().getSqlName() + "_2_" + $m.getTo().getSqlName();
								String $query = String.format("SELECT %s.* FROM %s JOIN %s ON %s = %s.%s WHERE %s = ?",
									$dbAccess.getDatabaseExtravaganza().escapeName($otherTable),
									$dbAccess.getDatabaseExtravaganza().escapeName($relationTable),
									$dbAccess.getDatabaseExtravaganza().escapeName($otherTable),
									$dbAccess.getDatabaseExtravaganza().escapeName("f_" + $otherTable),
									$dbAccess.getDatabaseExtravaganza().escapeName($otherTable),
									$dbAccess.getDatabaseExtravaganza().escapeName("id"),
									$dbAccess.getDatabaseExtravaganza().escapeName("f_" + $interface.getSqlName())
								);
								@SuppressWarnings("unchecked")
								RecordSet<? extends ActiveRecord<?>> $result = $dbAccess.select((Class<? extends ActiveRecord<?>>) $property.getGenericType(), $query, $id);
								@SuppressWarnings({"unchecked", "unused"})
								SetList<? extends ActiveRecord<?>> $return = $resolvedSets.put($property.getSqlName(), (SetList<ActiveRecord<?>>) $result);
								return $result;
							}
						}
						// info: Illegal many-to-many relationship
						return new RecordSetImpl<ActiveRecord<?>>($dbAccess, null, null);
					}
					@SuppressWarnings("unchecked")
					Class<ActiveRecord<?>> $recordType = (Class<ActiveRecord<?>>) $property.getGenericType();
					String $onePart = $property.getOnePart().getSqlName();
					SetList<ActiveRecord<?>> $records = $dbAccess.select($recordType,
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
			return null;
			
		} else if ($methodName.startsWith("set")) {
			String $propertyName = $methodName.substring(3);
			String $beanPropertyName = Character.toLowerCase($methodName.charAt(3)) + $methodName.substring(4);
			Property $property = $interface.getProperty($propertyName);
			$propertyName = DatabaseSQL.makeSQLName($propertyName);
			if ($args[0] instanceof ActiveRecord) {
				$resolvedRecords.put($propertyName, (ActiveRecord<?>) $args[0]);
				$args[0] = ((ActiveRecord<?>)$args[0]).getId();
			} else if ($property.getGenericType() != null) {
				@SuppressWarnings("unchecked")
				SetList<ActiveRecord<?>> $set = (SetList<ActiveRecord<?>>) $args[0];
				$resolvedSets.put($property.getSqlName(), $set);
			}
			Object $oldValue = null;
			if ($newValues.containsKey($propertyName)) {
				$oldValue = $newValues.get($propertyName);
			} else if ($oldValues.containsKey($propertyName)) {
				$oldValue = $oldValues.get($propertyName);
			}
			$newValues.put($propertyName, $args[0]);
			if ($propertyChangeSupport != null) {
				$propertyChangeSupport.firePropertyChange($beanPropertyName, $oldValue, $newValues.get($propertyName));
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
				for (Property $p : $interface.getProperties()) {
					if ($p.isManyToManyPart()) {
						String $sqlName = $p.getSqlName();
						if ($resolvedSets.containsKey($sqlName)) {
							Set<Integer> $ids = new TreeSet<Integer>();
							for (ActiveRecord<?> $record : $resolvedSets.get($sqlName)) {
								if (!$record.exists()) {
									$record.saveChanges();
								}
								$ids.add($record.getId());
							}
							// TODO: Check the following line for possible misbehaviour
							ManyToMany $m = $p.getManyToManyRelationships().iterator().next();
							String $iName = "f_" + $interface.getSqlName();
							String $fName = "f_" + $m.getTheOther($p).getParent().getSqlName();
							String $rName = $m.getFrom().getSqlName() + "_2_" + $m.getTo().getSqlName();
							String $query = String.format("SELECT %s FROM %s WHERE %s = ?",
								$dbAccess.getDatabaseExtravaganza().escapeName($fName),
								$dbAccess.getDatabaseExtravaganza().escapeName($rName),
								$dbAccess.getDatabaseExtravaganza().escapeName($iName)
							);
							RecordSet<?> $records = $dbAccess.query($query, $id);
							Set<Integer> $realIds = new TreeSet<Integer>();
							for (ActiveRecord<?> $record : $records) {
								$realIds.add((Integer) $record.get($fName));
							}
							Set<Integer> $idsToRemove = new TreeSet<Integer>();
							$idsToRemove.addAll($realIds);
							$idsToRemove.removeAll($ids);
							$ids.removeAll($realIds);
							
							if ($idsToRemove.size() > 0) {
								String[] $where = new String[$idsToRemove.size()];
								int $j = 0;
								for (Integer $oldId : $idsToRemove) {
									$where[$j] = $dbAccess.getDatabaseExtravaganza().escapeName($fName) + " = " + $oldId;
									$j++;
								}
								$query = String.format("DELETE FROM %s WHERE %s = ? AND (%s)",
										$dbAccess.getDatabaseExtravaganza().escapeName($rName),
										$dbAccess.getDatabaseExtravaganza().escapeName($iName),
										AbusingStrings.implode(" OR ", $where)
									);
								$dbAccess.exec($query, $id);
							}
							if ($ids.size() > 0) {
								for (Integer $newId : $ids) {
									$query = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
										$dbAccess.getDatabaseExtravaganza().escapeName($rName),
										$dbAccess.getDatabaseExtravaganza().escapeName($iName),
										$dbAccess.getDatabaseExtravaganza().escapeName($fName));
									$dbAccess.exec($query, $id, $newId);
								}
							}
						}
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
			if (($interface != null) && $interface.hasToStringProperty()) {
				String $name = DatabaseSQL.makeSQLName($interface.getToStringProperty().getName());
				if ($oldValues.containsKey($name)) {
					return $oldValues.get($name);
				} else if ($newValues.containsKey($name)) {
					return $newValues.get($name);
				}
			}
			return $interface.getName() + '#' + $id;
			
		} else if ($methodName == "databaseAccess") {
			return $dbAccess;
			
		} else if ($methodName == "newValues") {
			return Collections.unmodifiableMap($newValues);
			
		} else if ($methodName == "keys") {
			Set<String> $keys = new HashSet<String>($oldValues.keySet());
			$keys.addAll($newValues.keySet());
			return $keys.toArray(new String[$keys.size()]);
			
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
