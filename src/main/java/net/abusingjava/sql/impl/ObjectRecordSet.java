package net.abusingjava.sql.impl;

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
@Version("2011-08-15")
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
	}

	@Override
	public void saveChanges() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

}
