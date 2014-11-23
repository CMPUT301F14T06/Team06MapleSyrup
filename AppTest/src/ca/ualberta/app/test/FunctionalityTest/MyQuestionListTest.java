package ca.ualberta.app.test.FunctionalityTest;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class MyQuestionListTest extends TestCase {
	//test add Question to MyQuestionList
	public void testAddToMyQuestionList(){
		//create a Author object
		String userName = "TestUserName";
		Author author = new Author(userName);
		AuthorMapManager authorMapManager = new AuthorMapManager();
		authorMapManager.addAuthor(author);
		
		//create a Question object
		String questionString = "A Question";
		String titleString = "title";
		String imageByteArray = null;
		Question question = new Question(questionString, userName, titleString,
				imageByteArray);
		
		//add the QuestionID to the Author
		author.addAQuestion(question.getID());
		authorMapManager.updateAuthor(author);
		
		//check the result
		Author result = authorMapManager.getAuthor(userName);	
		assertTrue(result.getAuthorQuestionId().size() == 1);
		
		authorMapManager.deleteAuthor(userName);
	}
}
