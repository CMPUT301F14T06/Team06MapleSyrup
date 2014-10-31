package ca.ualberta.app.activity.test;

import java.util.Map;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import junit.framework.TestCase;

public class AuthorMapTest extends TestCase {
	public void testAuthorMap() {
		AuthorMap authorMap = new AuthorMap();
		Map<String, Author> theMap = authorMap.getMap();
		assertEquals("Empty Author map", theMap.size(), 0);
	}

	public void testAddAuthor() {
		String loginNameString = "loginName";
		AuthorMap authorMap = new AuthorMap();
		authorMap.addAuthor(loginNameString);
		authorMap.addAuthor(loginNameString);
		Map<String, Author> theMap = authorMap.getMap();
		assertEquals("Author Map Size", theMap.size(), 1);
	}

	public void testAuthorExist() {
		String loginNameString = "loginName";
		AuthorMap authorMap = new AuthorMap();
		authorMap.addAuthor(loginNameString);
		Map<String, Author> theMap = authorMap.getMap();
		assertEquals("Answer Map Size", theMap.size(), 1);
		assertTrue("Author doesnt exist",
				authorMap.getMap().get(loginNameString) != null);
	}
}
