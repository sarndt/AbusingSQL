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
@Version("2011-08-13")
@Target(ElementType.METHOD)
public @interface ToString {

}
