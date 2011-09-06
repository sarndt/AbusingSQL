package net.abusingjava.sql;

public interface Mixin<T extends ActiveRecord<?>> {

	void setDatabaseAccess(DatabaseAccess $dbAccess);
	
	void setActiveRecord(T $activeRecord);
}
