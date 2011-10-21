package net.abusingjava.sql;

import net.abusingjava.AbusingStrings;
import net.abusingjava.Author;
import net.abusingjava.Version;
import net.abusingjava.sql.impl.GenericDatabaseAccessFactory;

@Author("Julian Fleischer")
@Version("2011-08-16")
final public class AbusingSQL {

	AbusingSQL() {}
	
	public static DatabaseAccessFactory newDatabaseAccessFactory(final String $name) {
		try {
			return (DatabaseAccessFactory) Class.forName($name).newInstance();
		} catch (Exception $exc) {
			throw new RuntimeException($exc);
		}
	}

	public static <T extends DatabaseAccessFactory> T newDatabaseAccessFactory(final Class<T> $class) {
		try {
			return $class.newInstance();
		} catch (Exception $exc) {
			throw new RuntimeException($exc);
		}
	}

	public static DatabaseAccessFactory newDatabaseAccessFactory(final DatabaseExtravaganza $extravaganza, final Class<?>... $classes) {
		return new GenericDatabaseAccessFactory($extravaganza, $classes);
	}
	
	private static String debugValue(final Object $value) {
		if ($value instanceof String) {
			return '"' + $value.toString() + '"';
		}
		return $value.toString();
	}
	
	public static String debugQuery(final String $query, final Object... $values) {
		int $offset = 0;
		String[] $pieces = new String[($values.length*2)+1];
		for (int $i = 0; $i < $values.length; $i++) {
			int $index = $query.indexOf('?', $offset);
			$pieces[$i*2] = $query.substring($offset, $index);
			$pieces[($i*2)+1] = debugValue($values[$i]);
			$offset = $index+1;
		}
		$pieces[$pieces.length-1] = $query.substring($offset);
		return AbusingStrings.implode("", $pieces);
	}
}
