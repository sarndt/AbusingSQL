package net.abusingjava.sql.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.sql.DatabaseAccess;
import net.abusingjava.sql.Interface;

@Author("Julian Fleischer")
@Since("1.0")
public class ActiveRecordUpdateHandler implements InvocationHandler {

	final DatabaseAccess $dbAccess;
	final Interface $interface;
	
	public ActiveRecordUpdateHandler(final DatabaseAccess $dbAccess, final Interface $interface) {
		this.$dbAccess = $dbAccess;
		this.$interface = $interface;
	}

	@Override
	public Object invoke(final Object $proxy, final Method $method, final Object[] $args) throws Throwable {
		return $proxy;
	}

}
