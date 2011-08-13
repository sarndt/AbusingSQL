package net.abusingjava.sql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.abusingjava.sql.*;
import net.abusingjava.strings.AbusingStrings;

public class DatabaseMySQL extends AbstractDatabaseExtravaganza {

	@Override
	public String escapeName(final String $name) {
		return '`' + $name + '`';
	}

	@Override
	public Object get(final ResultSet $resultSet, final int $index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(final ResultSet $resultSet, final String $name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDriverName() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public void createDatabase(final ConnectionPool $pool, final Schema $schema) {
		try {
			Connection $c = $pool.getConnection();
			
			$c.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
			$c.setAutoCommit(false);
			try {
				StringBuilder $b = new StringBuilder();
				
				for (Interface $i : $schema.getInterfaces()) {
					$b.append("CREATE TABLE ");
					$b.append(escapeName($i.getSqlName()));
					$b.append(" (\n");
					$b.append("\t`id` INTEGER AUTO_INCREMENT,\n\t");
					for (Property $p : $i.getProperties()) {
						if ($p.getGenericType() != null) {
							continue;
						}
						$b.append(escapeName($p.getSqlName()));
						$b.append(" ");
						$b.append(getSqlType($p));
						if (!$p.isNullable()) {
							$b.append(" NOT NULL");
						}
						$b.append(",\n\t");
					}
					for (Property $p : $i.getProperties()) {
						if ($p.isManyPart()) {
							$b.append("FOREIGN KEY (`");
							$b.append($p.getSqlName());
							$b.append("`) REFERENCES `");
							$b.append($schema.getInterface($p.getJavaType().getCanonicalName()).getSqlName());
							$b.append("` (`id`)");
							$b.append(",\n\t");
						}
					}
					for (UniqueKey $key : $i.getUniqueKeys()) {
						$b.append("UNIQUE (");
						Property[] $properties = $key.getProperties();
						String[] $propertyNames = new String[$properties.length];
						for (int $j = 0; $j < $propertyNames.length; $j++) {
							$propertyNames[$j] = escapeName($properties[$j].getSqlName());
						}
						$b.append(AbusingStrings.implode(", ", $propertyNames));
						$b.append("),\n\t");
					}
					$b.append("PRIMARY KEY (`id`)\n) ENGINE=InnoDB;\n");
	
					$c.createStatement().execute($b.toString());
					$b.setLength(0);
				}
				
				for (ManyToMany $m : $schema.getManyToManyRelationships()) {
					$b.append("CREATE TABLE `");
					$b.append($m.getFrom().getSqlName());
					$b.append("_2_");
					$b.append($m.getTo().getSqlName());
					$b.append("`(\n\t");
					
					$b.append("`f_");
					$b.append($m.getFrom().getSqlName());
					$b.append("` INTEGER NOT NULL");
					$b.append(",\n\t");
					
					$b.append("`f_");
					$b.append($m.getTo().getSqlName());
					$b.append("` INTEGER NOT NULL");
					$b.append(",\n\t");
					
					$b.append("PRIMARY KEY (");
					$b.append("`f_");
					$b.append($m.getFrom().getSqlName());
					$b.append("`, `f_");
					$b.append($m.getTo().getSqlName());
					$b.append("`)");
					
					$b.append("\n) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COLLATE = utf8_unicode_ci;\n");
	
					$c.createStatement().execute($b.toString());
					$b.setLength(0);
				}
				
				$c.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
				$c.commit();
			} catch (SQLException $exc) {
				throw new DatabaseException($exc);
			} finally {
				$pool.release($c);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

	@Override
	public String getSqlType(final Property $property) {
		Class<?> $javaType = $property.getJavaType();
		StringBuilder $builder = new StringBuilder();
		
		if (($javaType == Integer.class) || ($javaType == int.class)) {
			$builder.append("INTEGER");
			if ($property.getDefault() != null) {
				$builder.append(" DEFAULT ");
				$builder.append($property.getDefault().toString());
				$builder.append(" ");
			}
		} else if (($javaType == Boolean.class) || ($javaType == boolean.class)) {
			$builder.append("TINYINT(1)");
			if ($property.getDefault() != null) {
				$builder.append(" DEFAULT ");
				$builder.append($property.getDefault().toString());
				$builder.append(" ");
			}
		} else if ($javaType == String.class) {
			if ($property.getMax() != null) {
				$builder.append("VARCHAR(");
				$builder.append($property.getMax().toString());
				$builder.append(")");
			} else {
				$builder.append("TEXT");
			}
		} else if ($javaType == Date.class) {
			$builder.append("DATETIME");
		} else if ($javaType == byte[].class) {
			if ($property.getMax() != null) {
				$builder.append("VARBINARY(");
				$builder.append($property.getMax().toString());
				$builder.append(")");
			} else {
				$builder.append("VARBINARY(32)");
			}
		} else if ($javaType.isEnum()) {
			$builder.append("ENUM('");
			Enum<?>[] $enums = (Enum[]) $javaType.getEnumConstants();
			String[] $enumNames = new String[$enums.length];
			for (int $i = 0; $i < $enums.length; $i++) {
			        $enumNames[$i] = $enums[$i].name();
			}
			$builder.append(AbusingStrings.implode("', '", $enumNames));
			$builder.append("')");
		} else if (ActiveRecord.class.isAssignableFrom($javaType)) {
			$builder.append("INTEGER");
		}
		
		return $builder.toString();
	}

	@Override
	public void dropDatabase(final ConnectionPool $pool, final Schema $schema) {
		try {
			Connection $c = $pool.getConnection();
			
			$c.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
			$c.setAutoCommit(false);
			try {
				StringBuilder $b = new StringBuilder();
				
				for (ManyToMany $m : $schema.getManyToManyRelationships()) {
					$b.append("DROP TABLE IF EXISTS `");
					$b.append($m.getFrom().getSqlName());
					$b.append("_2_");
					$b.append($m.getTo().getSqlName());
					$b.append("`");
	
					$c.createStatement().execute($b.toString());
					$b.setLength(0);
				}
				for (Interface $i : $schema.getInterfaces()) {
					$b.append("DROP TABLE IF EXISTS ");
					$b.append(escapeName($i.getSqlName()));
					$b.append(" CASCADE");
					
					$c.createStatement().execute($b.toString());
					$b.setLength(0);
				}
				$c.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
				$c.commit();
			} catch (SQLException $exc) {
				throw new DatabaseException($exc);
			} finally {
				$pool.release($c);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}

}
