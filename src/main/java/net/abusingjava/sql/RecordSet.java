package net.abusingjava.sql;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Set;

public class RecordSet<T extends ActiveRecord<?>> extends LinkedList<T> implements Set<T> {

	private static final long serialVersionUID = -1889746615690043280L;

	final DatabaseAccess $dbAccess;
	
	RecordSet(final DatabaseAccess $dbAccess, final ResultSet $result, final Class<T> $class) throws SQLException {
		this.$dbAccess = $dbAccess;
		while ($result.next()) {
			@SuppressWarnings("unchecked")
			T $instance = (T) Proxy.newProxyInstance($class.getClassLoader(), new Class<?>[] { $class },
					new ActiveRecordHandler($dbAccess, $class, $result));
			add($instance);
		}
	}
	
	public void saveChanges() {
		$dbAccess.beginTransaction();
		for (T $obj : this) {
			if ($obj.hasChanges()) {
				$obj.saveChanges();
			}
		}
		$dbAccess.commitTransaction();
	}
	
	public void deleteAll() {
		$dbAccess.beginTransaction();
		for (T $obj : this) {
			$obj.delete();
		}
		$dbAccess.commitTransaction();
	}

}
