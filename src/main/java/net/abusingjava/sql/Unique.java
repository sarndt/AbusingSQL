package net.abusingjava.sql;

import java.lang.annotation.*;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Author("Julian Fleischer")
@Version("2011-09-06")
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Unique {
	String value() default "";
}
