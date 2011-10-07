package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-09-06")
public abstract class AbstractMixin<T extends ActiveRecord<T>> implements Mixin<T> {

	private DatabaseAccess $db = null;
	private T $record = null;
	
	@Override
	public void setDatabaseAccess(final DatabaseAccess $dbAccess) {
		$db = $dbAccess;
	}
	
	protected DatabaseAccess db() {
		return $db;
	}

	@Override
	public void setActiveRecord(final T $activeRecord) {
		$record = $activeRecord;
	}
	
	protected T record() {
		return $record;
	}
}
