package net.abusingjava.sql.v1;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-15")
public interface Credentials {

	void setHost(String $host);
	String getHost();
	
	void setUsername(String $user);
	String getUsername();
	
	void setPassword(String $password);
	String getPassword();
	
	void setDatabaseName(String $databaseName);
	String getDatabaseName();
	
	String getJdbcUrl();
}
