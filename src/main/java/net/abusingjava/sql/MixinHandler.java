package net.abusingjava.sql;

import java.lang.annotation.*;

import net.abusingjava.Author;
import net.abusingjava.Version;

/**
 * Defines which class is responsible for realizing the Mixin.
 * 
 * @see AbstractMixin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Author("Julian Fleischer")
@Version("2011-09-06")
@Target(ElementType.TYPE)
public @interface MixinHandler {

	/**
	 * The class which contains the real code for the Mixin, obligatory.
	 */
	Class<? extends Mixin<?>> value();
	
}
