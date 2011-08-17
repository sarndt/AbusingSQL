package net.abusingjava.sql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Experimental;
import net.abusingjava.Version;
import net.abusingjava.sql.ConnectionProvider;
import net.abusingjava.sql.Property;
import net.abusingjava.sql.Schema;

@Author("Julian Fleischer")
@Version("2011-08-15")
@Experimental
public class DatabasePostgreSQL extends AbstractDatabaseExtravaganza {

	@Override
	public String escapeName(final String $name) {
		return '"' + $name + '"';
	}

	@Override
	public String getDriverName() {
		return "org.postgresql.Driver";
	}

	@Override
	public void createDatabase(final ConnectionProvider $pool, final Schema $schema) {
		
	}
	
	@Override
	public void dropDatabase(final ConnectionProvider $pool, final Schema $schema) {
		
	}

	@Override
	public String getSqlType(final Property $property) {
		return null;
	}

	@Override
	public int doInsert(final Connection $c, final String $table, final String[] $properties, final Object[] $values) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void doUpdate(final Connection $c, final String $table, final String[] $properties, final Object[] $values, final int $id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doDelete(final Connection $c, final String $table, final int $id) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object get(final ResultSet $resultSet, final String $column, final Class<?> $javaType) {
		// TODO Auto-generated method stub
		return null;
	}
}
