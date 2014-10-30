package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.InputListManager;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class InputLIstManagerTest extends TestCase {
	public void testAddQuestion(){
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		InputListManager ILM = new InputListManager();
		ILM.addQuestion(question);
		assertEquals("title",titleString);
	}
}
