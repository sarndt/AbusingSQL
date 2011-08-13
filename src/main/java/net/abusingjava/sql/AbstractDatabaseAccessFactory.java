package net.abusingjava.sql;

import java.sql.SQLException;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.schema.Schema;

@Author("Julian Fleischer")
@Version("2011-08-08")
public abstract class AbstractDatabaseAccessFactory implements DatabaseAccessFactory {
	
	int $reaperDelay = 25000;
	int $reaperTimeout = 30000;
	int $connectionTimeout = 1000;
	int $poolsize = 2;
	
	protected final Schema $schema;
	
	protected AbstractDatabaseAccessFactory() {
		$schema = new Schema();
	}
	
	void setReaperDelay(final int $delay) {
		$reaperDelay = $delay;
	}
	
	void setReaperTimeout(final int $timeout) {
		$reaperTimeout = $timeout;
	}
	
	void setConnectionTimeout(final int $timeout) {
		$connectionTimeout = $timeout;
	}
	
	@Override
	public void setPoolsize(final int $size) {
		$poolsize = $size;
	}
	
	@Override
	public Schema getSchema() {
		return $schema;
	}
	
	@Override
	public DatabaseAccess newDatabaseAccess(final String $url, final String $user, final String $password) {
		try {
			ConnectionPool $pool = new ConnectionPool("com.mysql.jdbc.Driver", $url, $user, $password, $poolsize, $reaperDelay, $reaperTimeout, $connectionTimeout);
			return new DatabaseAccessImpl($pool, $schema);
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		} catch (ClassNotFoundException $exc) {
			throw new RuntimeException($exc);
		}
	}
}
