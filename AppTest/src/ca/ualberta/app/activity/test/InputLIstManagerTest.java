package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.QuestionListManager;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class InputLIstManagerTest extends TestCase {
	
	public void testGetQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager ILM = new QuestionListManager();
		ILM.addQuestion(question);
		Question q = ILM.getQuestion(0);
		assertEquals(q.getContent(),question.getContent());
		assertEquals(q.getTitle(), question.getTitle());
	}

	public void testAddQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager ILM = new QuestionListManager();
		ILM.addQuestion(question);
		assertEquals(question,ILM.getQuestion(question.getID()));
	}
}
