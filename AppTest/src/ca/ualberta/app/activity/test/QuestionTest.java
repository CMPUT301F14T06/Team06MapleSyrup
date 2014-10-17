package ca.ualberta.app.activity.test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import junit.framework.TestCase;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;

public class QuestionTest extends TestCase {
	public void testQuestion() {
		String questionString = "A qustion";
		String authorString = "An Author";
		String titleString = "A title";
		ArrayList<Bitmap> imageList = new  ArrayList<Bitmap>();
		Question question = new Question(questionString, authorString,
				titleString, imageList);
		assertTrue("Question is not equal",
				questionString.equals(question.getContent()));
	}

	public void testaddAnswer() {
		String questionString = "A qustion";
		String answerString = "A answer";
		String authorString = "An Author";
		String titleString = "A title";
		ArrayList<Bitmap> imageList = new  ArrayList<Bitmap>();
		Answer answer = new Answer(answerString, authorString, imageList);
		Question question = new Question(questionString, authorString,
				titleString, imageList);
		question.addAnswer(answer);
		assertTrue("Answer is not equal",
				answerString.equals(answer.getContent()));
	}

	public void testaddReply() {
		String questionString = "A qustion";
		String replyString = "A reply";
		String authorString = "An Author";
		String titleString = "A title";
		ArrayList<Bitmap> imageList = new  ArrayList<Bitmap>();
		Reply reply = new Reply(replyString, authorString);
		Question question = new Question(questionString, authorString,
				titleString, imageList);
		question.addReply(reply);
		assertTrue("Reply is not equal", replyString.equals(reply.getContent()));
	}
}
