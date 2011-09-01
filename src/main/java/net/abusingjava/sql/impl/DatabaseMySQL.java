package net.abusingjava.sql.impl;

import static net.abusingjava.AbusingStrings.*;
import static net.abusingjava.functions.AbusingFunctions.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;
import net.abusingjava.sql.*;

@Author("Julian Fleischer")
@Version("2011-08-15")
@Since("1.0")
public class DatabaseMySQL extends AbstractDatabaseExtravaganza {

	@Override
	public String escapeName(final String $name) {
		return '`' + $name + '`';
	}

	@Override
	public String getDriverName() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public void createDatabase(final ConnectionProvider $pool, final Schema $schema) {
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
						$b.append(implode(", ", $propertyNames));
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
					$b.append(",\n\t");
					
					$b.append("FOREIGN KEY (`f_");
					$b.append($m.getFrom().getSqlName());
					$b.append("`) REFERENCES `");
					$b.append($m.getFrom().getSqlName());
					$b.append("` (`id`)");
					$b.append(",\n\t");

					$b.append("FOREIGN KEY (`f_");
					$b.append($m.getTo().getSqlName());
					$b.append("`) REFERENCES `");
					$b.append($m.getTo().getSqlName());
					$b.append("` (`id`)");
					
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
			$builder.append("DATETIME DEFAULT 0");
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
			$builder.append(implode("', '", $enumNames));
			$builder.append("')");
		} else if (ActiveRecord.class.isAssignableFrom($javaType)) {
			$builder.append("INTEGER");
		}
		
		return $builder.toString();
	}

	@Override
	public void dropDatabase(final ConnectionProvider $pool, final Schema $schema) {
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
					
					Statement $stmt = $c.createStatement();
					$stmt.execute($b.toString());
					$stmt.close();
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
        public void dropAllTablesInDatabase(final ConnectionProvider $pool) {
            try {
                
                Connection $c = $pool.getConnection();
                String $dbname= $pool.getSchemaNameFromURL();
                Statement $stmt = $c.createStatement();
                $stmt.execute("DROP DATABASE "+$dbname);
                $stmt.execute("CREATE DATABASE "+$dbname);
                $stmt.close();
            } catch (SQLException $exc) {
                throw new DatabaseException($exc);
            }
        }

        @Override
	public void doDelete(final Connection $c, final String $table, final int $id) throws SQLException {
		Statement $stmt = $c.createStatement();
		$stmt.executeUpdate("DELETE FROM `" + $table + "` WHERE `id` = " + $id);
		$stmt.close();
	}

	@Override
	public int doInsert(final Connection $c, final String $table, final String[] $properties, final Object[] $values) throws SQLException {
		PreparedStatement $stmt = $c.prepareStatement("INSERT INTO `" + $table + "` (`"
				+ implode("`, `", $properties) + "`) VALUES ("
				+ repeat("?, ", $properties.length - 1) + "?)"
				, Statement.RETURN_GENERATED_KEYS);
		prepare($stmt, $values);
		$stmt.executeUpdate();
		ResultSet $keys = $stmt.getGeneratedKeys();
		$keys.next();
		int $id = $keys.getInt(1);
		$keys.close();
		$stmt.close();
		return $id;
	}

	@Override
	public void doUpdate(final Connection $c, final String $table, final String[] $properties, final Object[] $values, final int $id) throws SQLException {
		PreparedStatement $stmt = $c.prepareStatement("UPDATE `" + $table + "` SET `"
				+ implode("` = ?, `", $properties) + "` = ? WHERE `id` = " + $id);
		prepare($stmt, $values);
		$stmt.executeUpdate();
		$stmt.close();
	}

	@Override
	public Object get(final ResultSet $resultSet, final String $column, final Class<?> $javaType) throws SQLException {
		Object $value = null;
		
		if (($javaType == int.class) || ($javaType == Integer.class)) {
			$value = $resultSet.getInt($column);
		} else if (($javaType == boolean.class) || ($javaType == Boolean.class)) {
			$value = $resultSet.getBoolean($column);
		} else if ($javaType == String.class) {
			$value = $resultSet.getString($column);
		} else if ($javaType == byte[].class) {
			$value = $resultSet.getBytes($column);
		} else if (($javaType == float.class) || ($javaType == Float.class)) {
			$value = $resultSet.getFloat($column);
		} else if (($javaType == double.class) || ($javaType == Double.class)) {
			$value = $resultSet.getDouble($column);
		} else if ($javaType.isEnum()) {
			$value = $resultSet.getString($column);
			if (!$resultSet.wasNull()) {
				return callback(Enum.class, "valueOf").call($javaType, $value);
			}
		} else if (($javaType == short.class) || ($javaType == Short.class)) {
			$value = $resultSet.getShort($column);
		} else if (($javaType == byte.class) || ($javaType == Byte.class)) {
			$value = $resultSet.getByte($column);
		} else if (java.util.Date.class.isAssignableFrom($javaType)) {
			try {
				$value = new Date($resultSet.getTimestamp($column).getTime());
			} catch (Exception $exc) {
				$value = null;
			}
		} else if (ActiveRecord.class.isAssignableFrom($javaType)) {
			$value = $resultSet.getInt($column);
		}
		if ($resultSet.wasNull()) {
			$value = null;
		}
		return $value;
	}

	@Override
	public void set(final PreparedStatement $stmt, final int $index, final Object $value) {
		try {
			if ($value instanceof Integer) {
				$stmt.setInt($index, (Integer)$value);
			} else if ($value instanceof String) {
				$stmt.setString($index, (String)$value);
			} else if ($value instanceof Boolean) {
				$stmt.setBoolean($index,  (Boolean)$value);
			} else if ($value instanceof Enum) {
				$stmt.setString($index, ((Enum<?>)$value).name());
			} else if ($value instanceof byte[]) {
				$stmt.setBytes($index, (byte[])$value);
			} else if ($value instanceof Date) {
				$stmt.setTimestamp($index, new java.sql.Timestamp( ((Date)$value).getTime() ));
			} else if ($value instanceof Float) {
				$stmt.setFloat($index, (Float)$value);
			} else if ($value instanceof Double) {
				$stmt.setDouble($index, (Double)$value);
			} else if ($value instanceof BigDecimal) {
				$stmt.setBigDecimal($index, (BigDecimal)$value);
			} else if ($value instanceof Short) {
				$stmt.setShort($index, (Short)$value);
			} else if ($value instanceof Byte) {
				$stmt.setByte($index, (Byte)$value);
			} else if ($value == null) {
				$stmt.setNull($index, 0);
			}
		} catch (SQLException $exc) {
			throw new DatabaseException($exc);
		}
	}
}
