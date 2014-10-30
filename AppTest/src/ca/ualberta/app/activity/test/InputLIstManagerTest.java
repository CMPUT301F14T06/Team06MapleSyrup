package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.InputListManager;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class InputLIstManagerTest extends TestCase {
	
	public void testGetQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		int ID = 1;
		Bitmap image = null;
		Question question = new Question(ID,questionString, userName, titleString,
				image);
		InputListManager ILM = new InputListManager();
		ILM.addQuestion(question);
		Question q = ILM.getQuestion(0);
		assertEquals(q.getContent(),question.getContent());
		assertEquals(q.getTitle(), question.getTitle());
	}

	public void testAddQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		int ID = 1;
		Bitmap image = null;
		Question question = new Question(ID,questionString, userName, titleString,
				image);
		InputListManager ILM = new InputListManager();
		ILM.addQuestion(question);
		assertEquals(question,ILM.getQuestion(question.getID()));
	}
}
