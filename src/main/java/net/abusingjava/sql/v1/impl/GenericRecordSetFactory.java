package net.abusingjava.sql.v1.impl;

import java.sql.ResultSet;

import net.abusingjava.sql.v1.ActiveRecord;
import net.abusingjava.sql.v1.RecordSet;
import net.abusingjava.sql.v1.RecordSetFactory;

public class GenericRecordSetFactory implements RecordSetFactory {

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> create(Class<T> $class) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ActiveRecord<T>> RecordSet<T> createFromResultSet(Class<T> $class, ResultSet $resultSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
