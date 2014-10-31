package ca.ualberta.app.activity.test;

import junit.framework.TestCase;
import ca.ualberta.app.models.Author;

public class AuthorTest extends TestCase {
	public void testAuthor() {
		String username = "username";
		String email = "A Eamil";
		Author author = new Author(username);
		author.setEmail(email);

		assertTrue("Author Login Name is not equal",
				username.equals(author.getUsername()));
		assertTrue("Author Email is not equal", email.equals(author.getEmail()));
	}
}
