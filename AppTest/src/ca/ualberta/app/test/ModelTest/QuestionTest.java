package ca.ualberta.app.test.ModelTest;

import junit.framework.TestCase;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;

public class QuestionTest extends TestCase {
	// test initial Question object
	public void testQuestion() {
		String username = "username";
		String questionString = "A qustion";
		String titleString = "A title";
		String imageByteArray = null;
		Author author = new Author(username);
		Question question = new Question(questionString, author.getUserId(),
				titleString, imageByteArray);
		assertEquals(questionString, question.getContent());
	}

	public void testAddAnswer() {
		// create a Question and an Answer object
		String username = "username";
		String questionString = "A qustion";
		String answerString = "A answer";
		String titleString = "A title";
		String imageByteArray = null;
		Author author = new Author(username);
		Answer answer = new Answer(answerString, author.getUserId(),
				imageByteArray);
		Question question = new Question(questionString, author.getUserId(),
				titleString, imageByteArray);

		// add the answer to Question
		question.addAnswer(answer);

		// check the result
		assertTrue("Answer is not equal",
				answerString.equals(answer.getContent()));
	}

	public void testAddReply() {
		// create a Question and a Reply Object
		String username = "username";
		String questionString = "A qustion";
		String replyString = "A reply";
		String titleString = "A title";
		String imageByteArray = null;
		Author author = new Author(username);
		Reply reply = new Reply(replyString, author.getUserId());
		Question question = new Question(questionString, author.getUserId(),
				titleString, imageByteArray);

		// add the reply to the Question
		question.addReply(reply);

		// check the result
		assertTrue("Reply is not equal", replyString.equals(reply.getContent()));
	}
}
