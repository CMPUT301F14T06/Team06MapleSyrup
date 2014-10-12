package ca.ualberta.app.activity.test;

import junit.framework.TestCase;
import ca.ualberta.app.models.Author;

public class AuthorTest extends TestCase {
	public void testAuthor() {
		String loginName = "loginname";
		String authorName = "an author";
		String email = "A Eamil";
		int userID = 123456;
		Author author = new Author(loginName, userID);
		author.setName(authorName);
		author.setEmail(email);

		assertTrue("Author Login Name is not equal",
				loginName.equals(author.getLoginName()));
		assertTrue("Author Name is not equal",
				authorName.equals(author.getAuthorName()));
		assertTrue("Author Email is not equal", email.equals(author.getEmail()));
		assertTrue("Author ID is not equal", userID == (author.getID()));
	}
}
