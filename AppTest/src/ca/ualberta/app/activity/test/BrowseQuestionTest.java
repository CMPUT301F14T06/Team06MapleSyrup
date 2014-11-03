package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;

import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionListManager;
import junit.framework.TestCase;

public class BrowseQuestionTest extends TestCase {
	public void testBrowseQuestionsOffline() {
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListController inputsListController = new QuestionListController();
		inputsListController.addQuestion(question);
		
		Question result = inputsListController.getQuestion(0);
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
	}
	
	public void testBrowseQuestionsOffline_extremeCase(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListController inputsListController = new QuestionListController();
		inputsListController.addQuestion(question);
		
		int position = inputsListController.getQuestionPosition(question);
		Question result = inputsListController.getQuestion(position);
		
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
	}
	
	public void testBrowseQuestionsOnline(){
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
}
