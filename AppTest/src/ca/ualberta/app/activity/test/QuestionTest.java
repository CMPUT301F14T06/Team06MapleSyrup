package ca.ualberta.app.activity.test;

import junit.framework.TestCase;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;

public class QuestionTest extends TestCase {
	public void testQuestion() {
		String questionString = "A qustion";
		String authorString = "An Author";
		String titleString = "A title";
		Question question = new Question(questionString, authorString,
				titleString);
		assertTrue("Question is not equal",
				questionString.equals(question.getContent()));
	}

	public void testaddAnswer() {
		String questionString = "A qustion";
		String answerString = "A answer";
		String authorString = "An Author";
		String titleString = "A title";
		Answer answer = new Answer(answerString, authorString);
		Question question = new Question(questionString, authorString,
				titleString);
		question.addAnswer(answer);
		assertTrue("Answer is not equal",
				answerString.equals(answer.getContent()));
	}

	public void testaddReply() {
		String questionString = "A qustion";
		String replyString = "A reply";
		String authorString = "An Author";
		String titleString = "A title";
		Reply reply = new Reply(replyString);
		Question question = new Question(questionString, authorString,
				titleString);
		question.addReply(reply);
		assertTrue("Reply is not equal", replyString.equals(reply.getReply()));
	}
}
