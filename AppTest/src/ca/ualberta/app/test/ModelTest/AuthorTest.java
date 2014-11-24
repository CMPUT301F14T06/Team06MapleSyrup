package ca.ualberta.app.test.ModelTest;

import junit.framework.TestCase;
import ca.ualberta.app.models.Author;

public class AuthorTest extends TestCase {
	//test Author model
	public void testAuthor() {
		String username = "username";
		Author author = new Author(username);

		assertTrue("Author Login Name is not equal",
				username.equals(author.getUsername()));
	}
	public void TestSetUsername(){
        String username = "username";
        String newUsername = "Robin";
	    Author author = new Author(username); 
        author.setUsername(newUsername);

        assertEquals(author.getUsername(), "Robin");
        
   }
}
