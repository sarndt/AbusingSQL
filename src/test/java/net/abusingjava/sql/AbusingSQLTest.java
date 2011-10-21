package net.abusingjava.sql;

import junit.framework.Assert;

import org.junit.Test;

public class AbusingSQLTest {

	@Test
	public void $1() throws Exception {
		String $query = AbusingSQL.debugQuery("SELECT * FROM test WHERE x = ?", "hallo welt");
		Assert.assertEquals("SELECT * FROM test WHERE x = \"hallo welt\"", $query);
	}
	
	@Test
	public void $2() throws Exception {
		String $query = AbusingSQL.debugQuery("SELECT * FROM test WHERE x = ? OR y = ?", "hallo welt", 7);
		Assert.assertEquals("SELECT * FROM test WHERE x = \"hallo welt\" OR y = 7", $query);
	}
	
	
}
