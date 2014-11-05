package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import junit.framework.TestCase;

public class QuestionListManagerTest extends TestCase {
	public void testAddQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager questionListManager = new QuestionListManager();
		questionListManager.addQuestion(question);
		Question result = questionListManager.getQuestion(question.getID());
		
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
		
		questionListManager.deleteQuestion(question.getID());
	}
	
	public void testGetQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager questionListManager = new QuestionListManager();
		questionListManager.addQuestion(question);
		Question result = questionListManager.getQuestion(question.getID());
		
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
		
		questionListManager.deleteQuestion(question.getID());
	}
	
	public void testGetQuestionList(){
		String questionString = "A Question";
		String userName = "TestUser";
		String titleString = "title";
		Bitmap image = null;
		Author author = new Author(userName);
		Question question1 = new Question(questionString, userName, titleString,
				image);
		Question question2 = new Question(questionString, userName, titleString,
				image);
		question2.setID(question1.getID() + 100);
		
		QuestionListManager questionListManager = new QuestionListManager();
		AuthorMapManager authorMapManager = new AuthorMapManager();
		
		author.addAQuestion(question1.getID());
		author.addAQuestion(question2.getID());
		authorMapManager.addAuthor(author);
		questionListManager.addQuestion(question1);
		questionListManager.addQuestion(question2);
		
		Author resultAuthor = authorMapManager.getAuthor(userName);
		QuestionList result = questionListManager.getQuestionList(resultAuthor.getAuthorQuestionId());
		Question resultQuestion1 = result.getQuestion(0);
		Question resultQuestion2 = result.getQuestion(1);
		
		assertEquals(2, result.size());
		assertEquals(question1.getContent(), resultQuestion1.getContent());
		assertEquals(question1.getAuthor(), resultQuestion1.getAuthor());
		assertEquals(question1.getTitle(), resultQuestion1.getTitle());
		assertEquals(question1.getID(), resultQuestion1.getID());
		
		assertEquals(question2.getContent(), resultQuestion2.getContent());
		assertEquals(question2.getAuthor(), resultQuestion2.getAuthor());
		assertEquals(question2.getTitle(), resultQuestion2.getTitle());
		assertEquals(question1.getID(), resultQuestion1.getID());
		
	}
	
}
