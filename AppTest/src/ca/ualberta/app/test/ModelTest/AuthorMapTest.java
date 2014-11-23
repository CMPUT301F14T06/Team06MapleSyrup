package ca.ualberta.app.test.ModelTest;

import java.util.Map;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import junit.framework.TestCase;

public class AuthorMapTest extends TestCase {
	//test get AuthorMap
	public void testGetAuthorMap() {
		AuthorMap authorMap = new AuthorMap();
		Map<String, Author> theMap = authorMap.getMap();
		assertEquals("Empty Author map", theMap.size(), 0);
	}

	//test add Author to the Map
	public void testAddAuthor() {
		//create an Author object and an AuthorMap
		String loginNameString = "loginName";
		AuthorMap authorMap = new AuthorMap();
		Author authorName = new Author(loginNameString);
		
		//add the Author to the map
		authorMap.addAuthor(authorName);
		
		//check the result
		Map<String, Author> theMap = authorMap.getMap();
		assertEquals("Author Map Size", theMap.size(), 1);
	}

	public void testAuthorExist() {
		//create an Author object and an AuthorMap
		String loginNameString = "loginName";
		Author authorName = new Author(loginNameString);
		AuthorMap authorMap = new AuthorMap();
		
		//add the Author to the map
		authorMap.addAuthor(authorName);
		
		//check the result
		Map<String, Author> theMap = authorMap.getMap();
		assertEquals("Answer Map Size", theMap.size(), 1);
		assertTrue("Author doesnt exist",
				authorMap.getMap().get(loginNameString) != null);
	}
}
