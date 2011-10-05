package net.abusingjava.sql.impl;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-13")
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
	
	public static String makeJaveName(final String $name) {
		StringBuilder $builder = new StringBuilder();
		boolean $convertToUpperCase = false;
		for (int $i = 0; $i < $name.length(); $i++) {
			char $char = $name.charAt($i);
			
			if ($char == '_') {
				$convertToUpperCase = true;
			} else {
				if ($convertToUpperCase) {
					$char = Character.toUpperCase($char);
					$convertToUpperCase = false;
				}
				$builder.append($char);
			}
		}
		return $builder.toString();
	}
}
