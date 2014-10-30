package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListManager;
import junit.framework.TestCase;

public class QuestionListManagerTest extends TestCase {
//	public void testGetQuestion(){
//		QuestionListManager ILM = new QuestionListManager();
//		Question q = ILM.getQuestion(1);
//		assertEquals("my name is AllEgg Zhang",q.getContent());
//		//assertEquals(q.getTitle(), titleString);
//	}
	public void testAddQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		QuestionListManager ILM = new QuestionListManager();
		ILM.addQuestion(question);
	}
}
