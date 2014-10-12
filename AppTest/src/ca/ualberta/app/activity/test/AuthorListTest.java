package ca.ualberta.app.activity.test;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorList;
import junit.framework.TestCase;

public class AuthorListTest extends TestCase {
	public void testAuthorList() {
		AuthorList authorlist = new AuthorList();
		List<Author> authorList = authorlist.getList();
		ArrayList<Author> authorArrayList = authorlist.getArrayList();
		assertTrue("Empty Author List", authorList.size() == 0);
		assertTrue("Empty Author Array List", authorArrayList.size() == 0);
	}

	public void testAddAuthor() {
		String loginNameString = "loginName";
		AuthorList authorlist = new AuthorList();
		authorlist.addAuthor(loginNameString);
		ArrayList<Author> authorArrayList = authorlist.getArrayList();
		assertTrue("Answer List Size", authorArrayList.size() == 1);
	}

	public void testAuthorExist() {
		String loginNameString = "loginName";
		AuthorList authorlist = new AuthorList();
		authorlist.addAuthor(loginNameString);
		ArrayList<Author> authorArrayList = authorlist.getArrayList();
		assertTrue("Answer List Size", authorArrayList.size() == 1);
		assertTrue("Author doesnt exist", authorlist.authorExist(loginNameString) == true);
	}
}
