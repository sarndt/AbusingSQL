package net.abusingjava.sql.impl;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.ActiveRecord;
import net.abusingjava.sql.DatabaseAccess;
import net.abusingjava.sql.RecordSet;

@Author("Julian Fleischer")
@Version("2011-08-17")
public class ObjectRecordSet extends LinkedList<ActiveRecord<?>> implements RecordSet<ActiveRecord<?>> {

	private static final long serialVersionUID = -2574917189429656406L;
	
	final DatabaseAccess $dbAccess;

	ObjectRecordSet(final DatabaseAccess $dbAccess, final ResultSet $result) throws SQLException {
		this.$dbAccess = $dbAccess;
		while ($result.next()) {
			ActiveRecord<?> $instance = (ActiveRecord<?>) Proxy.newProxyInstance(
					ActiveRecord.class.getClassLoader(),
					new Class<?>[] { ActiveRecord.class },
					new ActiveRecordHandler($dbAccess, $result));
			add($instance);
		}
		$result.close();
	}

	@Override
	public void saveChanges() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void discardChanges() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasChanges() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener $l) {
		
	}

	@Override
	public void removePropertyChangeListener(final PropertyChangeListener $l) {
		
	}

	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return null;
	}

}
