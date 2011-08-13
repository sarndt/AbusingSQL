package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.beans.SupportsPropertyChangeEvents;

/**
 * Jede OO-Repräsentation eines Datensatzes ist ein ActiveRecord, ein Objekt, das sich selbst in der Datenbank verwaltet.
 * Die maßgebliche Implementierung dieses Interfaces geschieht durch den ActiveRecordHandler.
 * 
 * @param <T> Die Typen die einen ActiveRecord implementieren, sollten ihre Klasse als Typ angeben, damit das Fluent Interface funktioniert.
 * @see <a href="http://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface in der Wikipedia</a>
 */
@Author("Julian Fleischer")
@Version("2011-08-03")
public interface ActiveRecord<T> extends SupportsPropertyChangeEvents {

	/**
	 * Gibt die ID dieses Objekts zurück.
	 * 
	 * getId() != null impliziert exists()
	 * 
	 * @return Die ID, oder, wenn das Objekt noch nicht existiert, <code>null</code>.
	 */
	Integer getId();
	
	/**
	 * Speichert Änderungen zurück in die Datenbank.
	 */
	T saveChanges();
	
	/**
	 * Speichert Änderungen und speichert auch Objekte die zu diesem in Beziehung stehen.
	 */
	T saveChanges(int $depth);
	
	/**
	 * Verwirft alle Änderungen.
	 */
	T discardChanges();
	
	/**
	 * Löscht das Objekt aus der Datenbank.
	 */
	void delete();

	/**
	 * Prüft, ob das Objekt in der Datenbank angelegt ist.
	 * 
	 * existst() impliziert getId() != null
	 */
	boolean exists();
	
	/**
	 * Prüft, ob Änderungen an dem Objekt vorliegen.
	 */
	boolean hasChanges();
	
	/**
	 * Gibt den Wert einer Spalte des zugrunde liegenden ResultSets zurück.
	 */
	Object get(String $column);
	
	/**
	 * Setzt den Wert einer benannten Spalte.
	 */
	T set(String $column, Object $value);
	
}
