package net.abusingjava.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabasePostgreSQL extends AbstractDatabaseExtravaganza {

	@Override
	public String escapeName(final String $name) {
		return '"' + $name + '"';
	}

	@Override
	public Object get(final ResultSet $resultSet, final int $index) {
		try {
			return $resultSet.getObject($index);
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

	@Override
	public Object get(final ResultSet $resultSet, final String $name) {
		try {
			return $resultSet.getObject($name);
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

}
