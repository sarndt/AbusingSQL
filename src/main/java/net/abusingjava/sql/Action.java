package net.abusingjava.sql;

import net.abusingjava.Author;
import net.abusingjava.Version;

@Author("Julian Fleischer")
@Version("2011-08-10")
public enum Action {
	RESTRICT, CASCADE, SET_NULL, NO_ACTION
}
