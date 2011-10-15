package net.abusingjava.sql.v1;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1911368105009846788L;

	public DatabaseException(final String $message, final Throwable $exc) {
		super($message, $exc);
	}
}
