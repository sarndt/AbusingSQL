package net.abusingjava.sql.v1;

public @interface Query {

	Dialect dialect();
	
	String queryString();
	
}
