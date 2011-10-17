package net.abusingjava.sql.v1;

import java.sql.Connection;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.event.State;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface ConnectionProvider {

	enum ConnectionProviderState implements State {
		INITIALIZED, READY, CLOSED
	}
	
	void setCredentials(Credentials $credentials);
	
	void open();
	
	Connection getConnection();

	void releaseConnection(Connection $connection);
	
	void close();
	
	ConnectionProviderState getState();
}
