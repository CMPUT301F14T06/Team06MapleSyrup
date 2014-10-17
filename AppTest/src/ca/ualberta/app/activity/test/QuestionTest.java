package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import junit.framework.TestCase;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;

public class QuestionTest extends TestCase {
	public void testQuestion() {
		String questionString = "A qustion";
		int userID = 123;
		String titleString = "A title";
		Bitmap image = null;
		Question question = new Question(questionString, userID,
				titleString, image);
		assertTrue("Question is not equal",
				questionString.equals(question.getContent()));
	}

	public void testaddAnswer() {
		String questionString = "A qustion";
		String answerString = "A answer";
		int userID = 123;
		String titleString = "A title";
		Bitmap image = null;
		Answer answer = new Answer(answerString, userID, image);
		Question question = new Question(questionString, userID,
				titleString, image);
		question.addAnswer(answer);
		assertTrue("Answer is not equal",
				answerString.equals(answer.getContent()));
	}

	public void testaddReply() {
		String questionString = "A qustion";
		String replyString = "A reply";
		int userID = 123;
		String titleString = "A title";
		Bitmap image = null;
		Reply reply = new Reply(replyString, userID);
		Question question = new Question(questionString, userID,
				titleString, image);
		question.addReply(reply);
		assertTrue("Reply is not equal", replyString.equals(reply.getContent()));
	}
}
