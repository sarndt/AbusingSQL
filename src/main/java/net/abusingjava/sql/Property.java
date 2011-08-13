package net.abusingjava.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import net.abusingjava.*;
import net.abusingjava.arrays.AbusingArrays;
import net.abusingjava.reflection.AbusingReflection;

@Author("Julian Fleischer")
@Version("2011-08-13")
public class Property {

	final String $name;
	final String $sqlName;
	final Class<?> $javaType;
	final Class<?> $genericType;
	final boolean $isOnePart;
	final boolean $isManyPart;
	final String $uniqueKey;
	final boolean $isNullable;
	final Interface $parent;
	final Long $min;
	final Long $max;
	
	Property(final Interface $parent, final Class<?> $class, final String $name) {
		this.$parent = $parent;
		this.$name = $name;
		this.$sqlName = DatabaseSQL.makeSQLName($name);
		
		try {
			Method $getter = $class.getMethod("get" + $name);
			$javaType = $getter.getReturnType();
			if (Set.class.isAssignableFrom($javaType)) {
				$genericType = AbusingReflection.getGenericBaseType($getter.getGenericReturnType());
				$isOnePart = false;
				$isManyPart = true;
			} else if (ActiveRecord.class.isAssignableFrom($javaType)) {
				$genericType = null;
				$isOnePart = true;
				$isManyPart = false;
			} else {
				$genericType = null;
				$isOnePart = false;
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
			this.$min = $min;
			this.$max = $max;
			this.$isNullable = !AbusingArrays.containsType($annotations, NotNull.class);
			if (AbusingArrays.containsType($annotations, Unique.class)) {
				Unique $key = AbusingArrays.firstOfType($annotations, Unique.class);
				this.$uniqueKey = $key.value();
			} else {
				this.$uniqueKey = null;
			}
		} catch (NoSuchMethodException $exc) {
			throw new NotGonnaHappenException($exc);
		}
	}
	
	public String getName() {
		return $name;
	}
	
	public Class<?> getJavaType() {
		return $javaType;
	}
	
	public Class<?> getGenericType() {
		return $genericType;
	}
	
	public boolean isOnePart() {
		return $isOnePart;
	}
	
	public boolean isManyPart() {
		return $isManyPart;
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
	
	public long getMin() {
		return $min;
	}
	
	public long getMax() {
		return $max;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
