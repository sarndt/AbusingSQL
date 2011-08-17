package net.abusingjava.sql.impl;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.*;

@Author("Julian Fleischer")
@Version("2011-08-15")
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
		$result.close();
	}
	
	@Override
	public void saveChanges() {
		Connection $c = $dbAccess.getConnection();
		try {
			$c.setAutoCommit(false);
			for (T $obj : this) {
				if ($obj.hasChanges()) {
					$obj.saveChanges($c);
				}
			}
			$c.commit();
			$c.setAutoCommit(true);
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		} finally {
			$dbAccess.release($c);
		}
	}
	
	@Override
	public void deleteAll() {
		Connection $c = $dbAccess.getConnection();
		try {
			$c.setAutoCommit(false);
			for (T $obj : this) {
				$obj.delete($c);
			}
			$c.commit();
			$c.setAutoCommit(true);
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		} finally {
			$dbAccess.release($c);
		}
	}

}
