package ca.ualberta.app.activity.test;

import junit.framework.TestCase;
import ca.ualberta.app.models.Author;

public class AuthorTest extends TestCase {
	public void testAuthor() {
		String userName = "userName";
		String email = "A Eamil";
		Author author = new Author(userName);
		author.setEmail(email);

		assertTrue("Author Login Name is not equal",
				userName.equals(author.getUserName()));
		assertTrue("Author Email is not equal", email.equals(author.getEmail()));
	}
}
