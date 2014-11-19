package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import junit.framework.TestCase;

public class QuestionListManagerTest extends TestCase {
	// test add Question to server
	public void testAddQuestion() {
		// create a Question object
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		String imageByteArray  = null;
		Question question = new Question(questionString, userName, titleString,
				imageByteArray);

		// add the Question to the server
		QuestionListManager questionListManager = new QuestionListManager();
		questionListManager.addQuestion(question);

		// check the result
		Question result = questionListManager.getQuestion(question.getID());
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());

		questionListManager.deleteQuestion(question.getID());
	}

	// test get Question from server
	public void testGetQuestion() {
		// create a Question object
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		String imageByteArray  = null;
		Question question = new Question(questionString, userName, titleString,
				imageByteArray);

		// get the Question from the server
		QuestionListManager questionListManager = new QuestionListManager();
		questionListManager.addQuestion(question);

		// check the result
		Question result = questionListManager.getQuestion(question.getID());
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());

		questionListManager.deleteQuestion(question.getID());
	}

	// test get a QuestionList from server
	public void testGetQuestionList() {
		// create two Question objects and an Author object
		String questionString = "A Question";
		String userName = "TestUser";
		String titleString = "title";
		String imageByteArray  = null;
		Author author = new Author(userName);
		Question question1 = new Question(questionString, userName,
				titleString, imageByteArray);
		Question question2 = new Question(questionString, userName,
				titleString, imageByteArray);
		question2.setID(question1.getID() + 100);

		// inital manager
		QuestionListManager questionListManager = new QuestionListManager();
		AuthorMapManager authorMapManager = new AuthorMapManager();

		// add the QuestionID to the Author
		author.addAQuestion(question1.getID());
		author.addAQuestion(question2.getID());

		// add the Author to the map
		authorMapManager.addAuthor(author);

		// add the Questions to the server
		questionListManager.addQuestion(question1);
		questionListManager.addQuestion(question2);

		// check the result
		Author resultAuthor = authorMapManager.getAuthor(userName);
		QuestionList result = questionListManager.getQuestionList(resultAuthor
				.getAuthorQuestionId());
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
