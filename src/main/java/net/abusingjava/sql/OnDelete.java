package net.abusingjava.sql;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Author("Julian Fleischer")
@Version("2011-08-13")
public @interface OnDelete {

	Action value();
	
}
