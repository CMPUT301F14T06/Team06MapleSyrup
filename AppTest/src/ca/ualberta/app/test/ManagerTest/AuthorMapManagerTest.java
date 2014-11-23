package ca.ualberta.app.test.ManagerTest;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.models.Author;
import junit.framework.TestCase;

public class AuthorMapManagerTest extends TestCase {
	// test add an Author to server
	public void testAddAuthor() {
		// create an Author object
		String userName = "TestUserName";
		Author author = new Author(userName);

		// add to server through Manager
		AuthorMapManager authorMapManager = new AuthorMapManager();
		authorMapManager.addAuthor(author);

		// get this Author from server to check if we actually add it to the
		// server
		Author result = authorMapManager.getAuthor(userName);
		assertEquals(author.getUsername(), result.getUsername());

		authorMapManager.deleteAuthor(userName);
	}

	// test get an Author to server
	public void testGetAuthor() {
		// create an Author object
		String userName = "TestUserName";
		Author author = new Author(userName);

		// add to server through Manager
		AuthorMapManager authorMapManager = new AuthorMapManager();
		authorMapManager.addAuthor(author);

		// get this Author from server to check if we actually add it to the
		// server
		Author result = authorMapManager.getAuthor(userName);
		assertEquals(author.getUsername(), result.getUsername());

		authorMapManager.deleteAuthor(userName);
	}
}
