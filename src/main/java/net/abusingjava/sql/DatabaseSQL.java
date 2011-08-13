package net.abusingjava.sql;

final public class DatabaseSQL {

	DatabaseSQL() {}
	
	public static String makeSQLName(String $name) {
		if ($name.indexOf('.') >= 0) {
			$name = $name.substring($name.lastIndexOf('.') + 1);
		}
		boolean $lastCharWasUpperCase = true;
		StringBuilder $builder = new StringBuilder();
		for (int $i = 0; $i < $name.length(); $i++) {
			char $char = $name.charAt($i);
			
			if (!$lastCharWasUpperCase && Character.isUpperCase($char)) {
				$builder.append('_');
			}
			$builder.append(Character.toLowerCase($char));
			
			$lastCharWasUpperCase = Character.isUpperCase($char);
		}
		return $builder.toString();
	}
}
