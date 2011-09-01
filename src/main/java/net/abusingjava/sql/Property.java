package net.abusingjava.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import net.abusingjava.*;
import net.abusingjava.sql.impl.DatabaseSQL;

@Author("Julian Fleischer")
@Version("2011-08-15")
public class Property {

	final String $name;
	final String $sqlName;
	final Class<?> $javaType;
	final Class<?> $genericType;
	final boolean $isManyPart;
	final String $uniqueKey;
	final boolean $isNullable;
	final Interface $parent;
	final Long $min;
	final Long $max;
	final boolean $isToStringProperty;
	final Object $default;
	final Set<ManyToMany> $manyToMany = new HashSet<ManyToMany>();
	Property $onePart = null;
	
	
	Property(final Interface $parent, final Class<?> $class, final String $name) {
		this.$parent = $parent;
		this.$name = $name;
		this.$sqlName = DatabaseSQL.makeSQLName($name);
		
		try {
			Method $getter = $class.getMethod("get" + $name);
			$javaType = $getter.getReturnType();
			if (Set.class.isAssignableFrom($javaType)) {
				$genericType = AbusingReflection.genericBaseType($getter.getGenericReturnType());
				$isManyPart = false;
			} else if (ActiveRecord.class.isAssignableFrom($javaType)) {
				$genericType = null;
				$isManyPart = true;
			} else {
				$genericType = null;
				$isManyPart = false;
			}
			Method $setter = $class.getMethod("set" + $name, $javaType);
			Annotation[] $annotations = AbusingArrays.concat(
					$getter.getAnnotations(),
					$setter.getAnnotations(),
					$setter.getParameterAnnotations()[0]
			);
			Long $min = null;
			Long $max = null;
			if (AbusingArrays.containsType($annotations, MinLength.class)) {
				$min = (long) AbusingArrays.firstOfType($annotations, MinLength.class).value();
			}
			if (AbusingArrays.containsType($annotations, MaxLength.class)) {
				$max = (long) AbusingArrays.firstOfType($annotations, MaxLength.class).value();
			}
			if (AbusingArrays.containsType($annotations, Min.class)) {
				$min = AbusingArrays.firstOfType($annotations, Min.class).value();
			}
			if (AbusingArrays.containsType($annotations, Max.class)) {
				$max = AbusingArrays.firstOfType($annotations, Max.class).value();
			}
			$isToStringProperty = AbusingArrays.containsType($annotations, ToString.class);
			this.$min = $min;
			this.$max = $max;
			this.$isNullable = !AbusingArrays.containsType($annotations, NotNull.class)
					&& !$javaType.isPrimitive();
			if (AbusingArrays.containsType($annotations, Unique.class)) {
				Unique $key = AbusingArrays.firstOfType($annotations, Unique.class);
				this.$uniqueKey = $key.value();
			} else {
				this.$uniqueKey = null;
			}
			if (AbusingArrays.containsType($annotations, Default.class)) {
				Default $default = AbusingArrays.firstOfType($annotations, Default.class);
				if (($javaType == boolean.class) || ($javaType == Boolean.class)) {
					this.$default = $default.booleanValue();
				} else if (($javaType == int.class) || ($javaType == Integer.class)) {
					this.$default = $default.intValue();
				} else {
					this.$default = null;
				}
			} else {
				$default = null;
			}
		} catch (NoSuchMethodException $exc) {
			throw new NotGonnaHappenException($exc);
		}
	}
	
	public Object getDefault() {
		return $default;
	}
	
	public String getName() {
		return $name;
	}

	public String getSqlName() {
		return $sqlName;
	}
	
	public Class<?> getJavaType() {
		return $javaType;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ActiveRecord<T>> Class<T> getRecordType() {
		return (Class<T>) $javaType;
	}
	
	public Class<?> getGenericType() {
		return $genericType;
	}
	
	public boolean isManyPart() {
		return $isManyPart;
	}
	
	public Property getOnePart() {
		if ($onePart == null) {
			for (Property $p : $parent.$parent.getInterface($genericType).getProperties()) {
				if (ActiveRecord.class.isAssignableFrom($p.getJavaType())) {
					$onePart = $p;
				}
			}
		}
		return $onePart;
	}
	
	public boolean isToStringProperty() {
		return $isToStringProperty;
	}
	
	public boolean isManyToManyPart() {
		return !$manyToMany.isEmpty();
	}
	
	public boolean isUnique() {
		return $uniqueKey != null;
	}
	
	public String getUniqueKeyName() {
		return $uniqueKey;
	}
	
	public boolean isNullable() {
		return $isNullable;
	}
	
	public Long getMin() {
		return $min;
	}
	
	public Long getMax() {
		return $max;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
