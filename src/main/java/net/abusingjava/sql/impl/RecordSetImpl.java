package net.abusingjava.sql.impl;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.ActiveRecord;
import net.abusingjava.sql.DatabaseAccess;
import net.abusingjava.sql.Interface;
import net.abusingjava.sql.RecordSet;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class RecordSetImpl<T extends ActiveRecord<?>> extends LinkedList<T> implements RecordSet<T> {

	private static final long serialVersionUID = -1889746615690043280L;

	final DatabaseAccess $dbAccess;
	
	RecordSetImpl(final DatabaseAccess $dbAccess, final ResultSet $result, final Interface $interface) throws SQLException {
		this.$dbAccess = $dbAccess;
		while ($result.next()) {
			@SuppressWarnings("unchecked")
			T $instance = (T) Proxy.newProxyInstance($interface.getJavaType().getClassLoader(),
					new Class<?>[] { $interface.getJavaType() },
					new ActiveRecordHandler($dbAccess, $interface, $result));
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
