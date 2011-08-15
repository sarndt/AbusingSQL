package net.abusingjava.sql.impl;

import java.sql.ResultSet;
import java.util.*;

import net.abusingjava.sql.ActiveRecord;
import net.abusingjava.sql.DatabaseAccess;
import net.abusingjava.sql.RecordSet;

public class ObjectRecordSet extends LinkedList<ActiveRecord<?>> implements RecordSet<ActiveRecord<?>> {

	private static final long serialVersionUID = -2574917189429656406L;

	public ObjectRecordSet(final DatabaseAccess $dbAccess, final ResultSet $result) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveChanges() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
