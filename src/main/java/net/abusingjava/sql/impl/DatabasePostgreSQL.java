package net.abusingjava.sql.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Experimental;
import net.abusingjava.Version;
import net.abusingjava.sql.*;

@Author("Julian Fleischer")
@Version("2011-08-13")
@Experimental
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

	@Override
	public String getDriverName() {
		return "org.postgresql.Driver";
	}

	@Override
	public void createDatabase(final ConnectionPool $pool, final Schema $schema) {
		
	}
	
	@Override
	public void dropDatabase(final ConnectionPool $pool, final Schema $schema) {
		
	}

	@Override
	public String getSqlType(final Property $property) {
		return null;
	}

	@Override
	public String getDeleteQuery(final Interface $interface, final int $id) {
		// TODO Auto-generated method stub
		return null;
	}

}
