package ca.ualberta.app.activity.test;

import junit.framework.TestCase;
import ca.ualberta.app.models.Author;

public class AuthorTest extends TestCase {
	public void testAuthor() {
		String userName = "loginname";
		String email = "A Eamil";
		int userID = 123456;
		Author author = new Author(userName, userID);
		author.setEmail(email);

		assertTrue("Author Login Name is not equal",
				userName.equals(author.getUserName()));
		assertTrue("Author Email is not equal", email.equals(author.getEmail()));
		assertTrue("Author ID is not equal", userID == (author.getID()));
	}
}
