package net.abusingjava.sql.impl;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import net.abusingjava.sql.ActiveRecord;
import net.abusingjava.sql.DatabaseAccess;
import net.abusingjava.sql.RecordSet;

public class RecordSetImpl<T extends ActiveRecord<?>> extends LinkedList<T> implements RecordSet<T> {

	private static final long serialVersionUID = -1889746615690043280L;

	final DatabaseAccess $dbAccess;
	
	RecordSetImpl(final DatabaseAccess $dbAccess, final ResultSet $result, final Class<T> $class) throws SQLException {
		this.$dbAccess = $dbAccess;
		while ($result.next()) {
			@SuppressWarnings("unchecked")
			T $instance = (T) Proxy.newProxyInstance($class.getClassLoader(), new Class<?>[] { $class },
					new ActiveRecordHandler($dbAccess, $class, $result));
			add($instance);
		}
	}
	
	@Override
	public void saveChanges() {
		$dbAccess.beginTransaction();
		for (T $obj : this) {
			if ($obj.hasChanges()) {
				$obj.saveChanges();
			}
		}
		$dbAccess.commitTransaction();
	}
	
	@Override
	public void deleteAll() {
		$dbAccess.beginTransaction();
		for (T $obj : this) {
			$obj.delete();
		}
		$dbAccess.commitTransaction();
	}

}
