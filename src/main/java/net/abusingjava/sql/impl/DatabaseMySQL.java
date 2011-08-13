package net.abusingjava.sql.impl;

import java.sql.ResultSet;

public class DatabaseMySQL extends AbstractDatabaseExtravaganza {

	@Override
	public String escapeName(final String $name) {
		return '`' + $name + '`';
	}

	@Override
	public Object get(final ResultSet $resultSet, final int $index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(final ResultSet $resultSet, final String $name) {
		// TODO Auto-generated method stub
		return null;
	}

}
