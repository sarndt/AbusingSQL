package net.abusingjava.sql.v1;

import java.sql.Connection;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface Transaction {

	enum TransactionState {
		UNCOMMITED, COMMITED, ERRORNEOUS
	}

	void commit();

	void rollback();

	TransactionState getState();
	
	Connection getConnection();
}
