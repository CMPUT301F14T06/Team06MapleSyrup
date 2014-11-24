package ca.ualberta.app.test.ModelTest;

import java.util.ArrayList;
import java.util.Date;

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
	
	public void testAddAquestion(){
        String username = "username";
	Author author = new Author(username);
       
        Long questionID1=new Date().getTime();
        Long questionID2=new Date().getTime();

        ArrayList<Long> myQuestionID= new ArrayList<Long>();

        author.addAQuestion(questionID1);
        author.addAQuestion(questionID2);
        assertEquals(questionID1,author.getAuthorQuestionId().get(0));
        assertEquals(author.getAuthorQuestionId().size(),2);
   }
	
	public void testSetUsername(){
        String username = "username";
        String newUsername = "Robin";
	    Author author = new Author(username); 
        author.setUsername(newUsername);

        assertEquals(author.getUsername(), "Robin");
        
   }
}
