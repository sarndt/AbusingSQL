package net.abusingjava.sql;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 6385168726234235762L;

	public DatabaseException() {
	}

	public DatabaseException(final String $message) {
		super($message);
	}

	public DatabaseException(final Throwable $exc) {
		super($exc);
	}

	public DatabaseException(final String $message, final Throwable $exc) {
		super($message, $exc);
	}

}
