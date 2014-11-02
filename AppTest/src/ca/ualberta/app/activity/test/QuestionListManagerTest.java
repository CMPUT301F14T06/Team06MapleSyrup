package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListManager;
import junit.framework.TestCase;

public class QuestionListManagerTest extends TestCase {
	public void testAddQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager ILM = new QuestionListManager();
		ILM.addQuestion(question);
		Question result = ILM.getQuestion(question.getID());
		assertEquals(question, result);
	}
	public void testGetQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager ILM = new QuestionListManager();
		ILM.addQuestion(question);
		Question result = ILM.getQuestion(question.getID());
		assertEquals(question, result);
	}
	
}
