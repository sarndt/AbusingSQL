package net.abusingjava.sql.v1.impl;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-16")
public class WrappedConnection implements Connection {

	private final Connection $wrappedConnection;
	private long $use = System.currentTimeMillis();

	public WrappedConnection(final Connection $connection) {
		$wrappedConnection = $connection;
	}

	public long getMostRecentUseTime() {
		return $use;
	}
	
	@Override
	public boolean isWrapperFor(final Class<?> $class) throws SQLException {
		$use = System.currentTimeMillis();
		return $class.equals($wrappedConnection.getClass()) || $wrappedConnection.isWrapperFor($class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unwrap(final Class<T> $class) throws SQLException {
		$use = System.currentTimeMillis();
		if ($class.equals($wrappedConnection.getClass())) {
			return (T) $wrappedConnection;
		}
		return $wrappedConnection.unwrap($class);
	}

	@Override
	public void clearWarnings() throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.close();
	}

	@Override
	public void commit() throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.commit();
	}

	@Override
	public Array createArrayOf(final String $typeName, final Object[] $elements) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createArrayOf($typeName, $elements);
	}

	@Override
	public Blob createBlob() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createSQLXML();
	}

	@Override
	public Statement createStatement() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createStatement();
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability)
			throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.createStruct(typeName, attributes);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getCatalog();
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getClientInfo();
	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getClientInfo(name);
	}

	@Override
	public int getHoldability() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getHoldability();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getMetaData();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.isClosed();
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.isReadOnly();
	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.isValid(timeout);
	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.nativeSQL(sql);
	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareCall(sql);
	}

	@Override
	public CallableStatement prepareCall(final String $sql, final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareCall($sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(final String $sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareCall($sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareStatement($sql);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int autoGeneratedKeys) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareStatement($sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int[] columnIndexes) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareStatement($sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final String[] columnNames) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareStatement($sql, columnNames);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareStatement($sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType,
			final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.releaseSavepoint(savepoint);
	}

	@Override
	public void rollback() throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.rollback();
	}

	@Override
	public void rollback(final Savepoint $savepoint) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.rollback($savepoint);
	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setAutoCommit(autoCommit);
	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setCatalog(catalog);
	}

	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setClientInfo(properties);
	}

	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setClientInfo(name, value);
	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setHoldability(holdability);
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setReadOnly(readOnly);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		$use = System.currentTimeMillis();
		return $wrappedConnection.setSavepoint(name);
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setTransactionIsolation(level);
	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		$use = System.currentTimeMillis();
		$wrappedConnection.setTypeMap(map);
	}

}
