package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import junit.framework.TestCase;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;

public class QuestionTest extends TestCase {
	public void testQuestion() {
		String userName = "userName";
		String questionString = "A qustion";
		String titleString = "A title";
		Bitmap image = null;
		Author author = new Author(userName);
		Question question = new Question(questionString, author.getUserName(), 
				titleString, image);
		assertEquals(questionString,question.getContent());
	}

	public void testAddAnswer() {
		String userName = "userName";
		String questionString = "A qustion";
		String answerString = "A answer";
		String titleString = "A title";
		Bitmap image = null;
		Author author = new Author(userName);
		Answer answer = new Answer(answerString, author.getUserName(), image);
		Question question = new Question(questionString, author.getUserName(),
				titleString, image);
		question.addAnswer(answer);
		assertTrue("Answer is not equal",
				answerString.equals(answer.getContent()));
	}

	public void testAddReply() {
		String userName = "userName";
		String questionString = "A qustion";
		String replyString = "A reply";
		String titleString = "A title";
		Bitmap image = null;
		Author author = new Author(userName);
		Reply reply = new Reply(replyString, author.getUserName());
		Question question = new Question(questionString, author.getUserName(),
				titleString, image);
		question.addReply(reply);
		assertTrue("Reply is not equal", replyString.equals(reply.getContent()));
	}
}
