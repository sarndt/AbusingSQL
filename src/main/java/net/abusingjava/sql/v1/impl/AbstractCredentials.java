package net.abusingjava.sql.v1.impl;

import net.abusingjava.sql.v1.Credentials;

public abstract class AbstractCredentials implements Credentials {

	private String $host;
	private String $username;
	private String $password;
	private String $databaseName;
	
	@Override
	public void setHost(final String $host) {
		this.$host = $host;
	}

	@Override
	public String getHost() {
		return $host;
	}

	@Override
	public void setUsername(final String $username) {
		this.$username = $username;
	}

	@Override
	public String getUsername() {
		return $username;
	}

	@Override
	public void setPassword(final String $password) {
		this.$password = $password;
	}

	@Override
	public String getPassword() {
		return $password;
	}

	@Override
	public void setDatabaseName(final String $databaseName) {
		this.$databaseName = $databaseName;
	}

	@Override
	public String getDatabaseName() {
		return $databaseName;
	}

}
