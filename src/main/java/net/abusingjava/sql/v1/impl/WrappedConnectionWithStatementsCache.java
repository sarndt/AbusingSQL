package net.abusingjava.sql.v1.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-17")
public class WrappedConnectionWithStatementsCache extends WrappedConnection {

	Map<String, PreparedStatement> $preparedStatements = new HashMap<String, PreparedStatement>();

	public WrappedConnectionWithStatementsCache(final Connection $connection) {
		super($connection);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql) throws SQLException {
		return prepareStatement($sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int $autoGeneratedKeys) throws SQLException {
		String $key = "(" + $autoGeneratedKeys + ")" + $sql;
		if ($preparedStatements.containsKey($key)) {
			return $preparedStatements.get($key);
		}
		PreparedStatement $preparedStatement = super.prepareStatement($sql);
		$preparedStatements.put($key, $preparedStatement);
		return $preparedStatement;
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int[] columnIndexes) throws SQLException {
		return super.prepareStatement($sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final String[] columnNames) throws SQLException {
		return super.prepareStatement($sql, columnNames);
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int $resultSetType,
			final int $resultSetConcurrency)
			throws SQLException {
		String $key = "(" + $resultSetType + "," + $resultSetConcurrency + ")" + $sql;
		if ($preparedStatements.containsKey($key)) {
			return $preparedStatements.get($key);
		}
		PreparedStatement $preparedStatement = super.prepareStatement($sql);
		$preparedStatements.put($key, $preparedStatement);
		return $preparedStatement;
	}

	@Override
	public PreparedStatement prepareStatement(final String $sql, final int $resultSetType,
			final int $resultSetConcurrency,
			final int $resultSetHoldability) throws SQLException {
		String $key = "(" + $resultSetType + "," + $resultSetConcurrency + "," + $resultSetHoldability + ")" + $sql;
		if ($preparedStatements.containsKey($key)) {
			return $preparedStatements.get($key);
		}
		PreparedStatement $preparedStatement = super.prepareStatement($sql);
		$preparedStatements.put($key, $preparedStatement);
		return $preparedStatement;
	}

	@Override
	public void close() throws SQLException {
		try {
			for (PreparedStatement $stmt : $preparedStatements.values()) {
				$stmt.close();
			}
		} finally {
			super.close();
		}
	}
}