package net.abusingjava.sql.v1.impl;

import java.sql.ResultSet;

import net.abusingjava.sql.v1.ActiveRecord;
import net.abusingjava.sql.v1.ActiveRecordFactory;

public class GenericActiveRecordFactory implements ActiveRecordFactory {

	@Override
	public <T extends ActiveRecord<T>> T create(Class<T> $class) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ActiveRecord<T>> T createFromResultSet(Class<T> $class, ResultSet $resultSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
