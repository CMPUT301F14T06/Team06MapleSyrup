package ca.ualberta.app.test;

import junit.framework.TestCase;
import ca.ualberta.app.Author;

public class AuthorTest extends TestCase {
	public void testAuthor(){
		String userName = "A Author";
		String email = "A Eamil";
		int userID = 123456;
		Author author = new Author();
		author.setName(userName);
		author.setEmail(email);
		author.setID(userID);
		
		assertTrue("Author Name is not equal", userName.equals(author.getUserName()));
		assertTrue("Author Email is not equal", email.equals(author.getEmail()));
		assertTrue("Author ID is not equal", userID == (author.getUserID()));
	}
}
