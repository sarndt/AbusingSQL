package net.abusingjava.sql.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.ActiveRecord;
import net.abusingjava.sql.DatabaseAccess;
import net.abusingjava.sql.RecordSet;
import net.abusingjava.sql.schema.Interface;

@Author("Julian Fleischer")
@Version("2011-08-15")
public class RecordSetImpl<T extends ActiveRecord<?>> extends LinkedList<T> implements RecordSet<T> {

	private static final long serialVersionUID = -1889746615690043280L;

	final private DatabaseAccess $dbAccess;
	final private PropertyChangeSupport $propertyChangeSupport = new PropertyChangeSupport(this);
	
	RecordSetImpl(final DatabaseAccess $dbAccess, final ResultSet $result, final Interface $interface) throws SQLException {
		this.$dbAccess = $dbAccess;
		if ($result != null) {
			if ($interface == null) {
				throw new IllegalArgumentException("$interface may not be null");
			}
			while ($result.next()) {
				@SuppressWarnings("unchecked")
				T $instance = (T) Proxy.newProxyInstance($interface.getJavaType().getClassLoader(),
						new Class<?>[] { $interface.getJavaType() },
						new ActiveRecordHandler($dbAccess, $interface, $result));
				add($instance);
			}
			$result.close();
		}
	}
	
	@Override
	public void saveChanges() {
		Connection $c = $dbAccess.getConnection();
		try {
			//$c.setAutoCommit(false);
			for (T $obj : this) {
				if ($obj.hasChanges()) {
					$obj.saveChanges($c);
				}
			}
			//$c.commit();
			//$c.setAutoCommit(true);
		//} catch (SQLException $exc) {
		//	throw new DatabaseException($exc);
		} finally {
			$dbAccess.release($c);
		}
	}
	
	@Override
	public boolean hasChanges() {
		for (T $obj : this) {
			if ($obj.hasChanges()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void discardChanges() {
		for (T $obj : this) {
			if ($obj.hasChanges()) {
				$obj.discardChanges();
			}
		}
	}
	
	@Override
	public void deleteAll() {
		Connection $c = $dbAccess.getConnection();
		try {
			//$c.setAutoCommit(false);
			for (T $obj : this) {
				$obj.delete($c);
			}
			//$c.commit();
			//$c.setAutoCommit(true);
		//} catch (SQLException $exc) {
		//	throw new DatabaseException($exc);
		} finally {
			$dbAccess.release($c);
		}
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener $l) {
		$propertyChangeSupport.addPropertyChangeListener($l);
	}

	@Override
	public void removePropertyChangeListener(final PropertyChangeListener $l) {
		$propertyChangeSupport.removePropertyChangeListener($l);
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return $propertyChangeSupport.getPropertyChangeListeners();
	}

}
