package net.abusingjava.sql;

import java.util.List;

import net.abusingjava.Author;
import net.abusingjava.Version;

/**
 * A SetList is both a Set, and a List. Additionally it implements Deque.
 */
@Author("Julian Fleischer")
@Version("2011-08-15")
public interface SetList<T> extends List<T> {

}
