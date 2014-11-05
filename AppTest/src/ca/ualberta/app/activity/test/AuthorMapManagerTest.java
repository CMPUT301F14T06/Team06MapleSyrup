package ca.ualberta.app.activity.test;


import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.models.Author;
import junit.framework.TestCase;

public class AuthorMapManagerTest extends TestCase {
	public void testAddAuthor(){
		String userName = "TestUserName";
		Author author =  new Author(userName);
		AuthorMapManager authorMapManager = new AuthorMapManager();
		authorMapManager.addAuthor(author);
		Author result = authorMapManager.getAuthor(userName);
		assertEquals(author.getUsername(), result.getUsername());

		
		authorMapManager.deleteAuthor(userName);
	}
	
	public void testGetAuthor(){
		String userName = "TestUserName";
		Author author =  new Author(userName);
		AuthorMapManager authorMapManager = new AuthorMapManager();
		authorMapManager.addAuthor(author);
		Author result = authorMapManager.getAuthor(userName);
		
		assertEquals(author.getUsername(), result.getUsername());

		
		authorMapManager.deleteAuthor(userName);
	}
}
