package net.abusingjava.sql;

import java.sql.Connection;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.impl.RecordSetImpl;

/**
 * Bietet grundlegende Funktionen für den Zugriff auf die Datenbank. DatabaseAccess-Objekte sind thread-safe.
 */
@Author("Julian Fleischer")
@Version("2011-08-03")
public interface DatabaseAccess {

	/**
	 * Fordere eine java.sql.Connection an.
	 */
	Connection getConnection();

	/**
	 * Gib eine Connection zurück an den zu Grunde liegenden Connection Pool.
	 */
	void release(final Connection $connection);

	/**
	 * Beginnt eine Transaction.
	 */
	void beginTransaction();
	
	/**
	 * Beendet eine Transaction druch einen Commit. Im Fehlerfall gibt es eine DatabaseException (unchecked).
	 * 
	 * Hinterher besteht sicher keine Transaction mehr.
	 */
	void commitTransaction();

	/**
	 * Setzt die Änderungen innerhalb einer Transaction zurück, ohne diese zu beenden.
	 */
	void rollbackTransaction();
	
	/**
	 * Bricht die Transaction zurück, also wie rollback() plus, dass man hinterher keine Transaction mehr offen hat.
	 */
	void abortTransaction();

	/**
	 * Führt ein Select in der durch $class spezifizierten Tabelle aus, joint optional die angegebenen $joinClasses.
	 */
	<T extends ActiveRecord<T>> RecordSetImpl<T> select(final Class<T> $class, Class<?>... $joinClasses);
	
	/**
	 * Wie select(), aber mit $limit.
	 */
	<T extends ActiveRecord<T>> RecordSetImpl<T> select(final Class<T> $class, int $limit, Class<?>... $joinClasses);
	
	/**
	 * Wie select(), aber mit $offset und $limit.
	 */
	<T extends ActiveRecord<T>> RecordSetImpl<T> select(final Class<T> $class, int $offset, int $limit, Class<?>... $joinClasses);
	
	/**
	 * Wie select(), aber mit einem selbst-definierten Query, der auch Fragezeichen-Parameter beinhalten kann (wird durch $values aufgefüllt).
	 * <p>
	 * Beispiel:<br />
	 * <code>$db.select(Mitarbeiter.class, "SELECT * FROM mitarbeiter WHERE vorname = ? AND nachname = ?", "Anton", "Blechdach");</code>
	 */
	<T extends ActiveRecord<T>> RecordSetImpl<T> select(final Class<T> $class, final String $query, Object... $values);
	
	/**
	 * 
	 */
	RecordSetImpl<ActiveRecord<?>> query(final String $query, Object... $values);
	
	/**
	 * Wählt ein Objekt mit der gegebenen $id aus, oder null, wenn das Objekt in der Datenbank nicht existiert.
	 */
	<T extends ActiveRecord<T>> T selectById(Class<T> $class, int $id);
	
	/**
	 * Erstellt einen neuen ActiveRecord von einem Interface.
	 * 
	 * @param $class Die Interface-Klasse
	 * @throws IllegalArgumentException Wenn $class kein Interface darstellt.
	 */
	<T extends ActiveRecord<T>> T create(final Class<T> $class);

	/**
	 * Löscht alle Tabellen in der Datenbank.
	 */
	void dropDatabase();
	
	/**
	 * Erstelle alle Tabellen der Datenbank neu.
	 */
	void createDatabase();

	/**
	 * Sucht genau ein Objekt oder null zurück. Funktioniert wie select(Class<?>, String $query, Object... $values).
	 */
	<T extends ActiveRecord<T>> T selectOne(Class<T> $class, String $query, Object... $values);
	
	/**
	 * Returns the Schema-object that describes this particular Database.
	 */
	Schema getSchema();
}
