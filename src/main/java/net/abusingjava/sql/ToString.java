package net.abusingjava.sql;

import java.lang.annotation.*;

import net.abusingjava.Author;
import net.abusingjava.Version;

/**
 * Used to designate a property as the String-representation of the Entity.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Author("Julian Fleischer")
@Version("2011-09-05")
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface ToString {
	
}
