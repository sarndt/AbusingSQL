package net.abusingjava.sql.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.ConnectionProvider;

@Author("Julian Fleischer")
@Version("2011-08-15")
public class ConnectionProviderWithQueryCache implements ConnectionProvider {

	static class ConnectionHandler implements InvocationHandler {

		Connection $connection;
		
		ConnectionHandler() {
			
		}
		
		@Override
		public Object invoke(final Object $proxy, final Method $method, final Object[] $args) throws Throwable {
			return $method.invoke($connection, $args);
		}
		
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Connection $c = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
				new Class<?>[] { Connection.class }, new ConnectionHandler());
		return $c;
	}

	@Override
	public boolean release(final Connection $connection) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
