package net.abusingjava.sql.v1;

import java.lang.annotation.*;

import net.abusingjava.Author;
import net.abusingjava.Since;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("1.0")
@Since(version = "1.0", value = "2011-10-16")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
	
	String query();
}
