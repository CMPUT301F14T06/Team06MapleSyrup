package ca.ualberta.app.test;

import junit.framework.TestCase;
import ca.ualberta.app.Answer;
import ca.ualberta.app.Question;
import ca.ualberta.app.Reply;

public class QuestionTest extends TestCase {
	public void testQuestion(){
		String questionString = "A qustion";
		Question question = new Question(questionString);
		assertTrue("Question is not equal", questionString.equals(question.getQuestion()));
	}
	
	public void testaddAnswer(){
		String questionString = "A qustion";
		String answerString = "A answer";
		Answer answer = new Answer(answerString);
		Question question = new Question(questionString);
		question.addAnswer(answer);
		assertTrue("Answer is not equal",answerString.equals(answer.getAnswer()));
	}
	
	public void testaddReply(){
		String questionString = "A qustion";
		String replyString = "A reply";
		Reply reply = new Reply(replyString);
		Question question = new Question(questionString);
		question.addReply(reply);
		assertTrue("Reply is not equal",replyString.equals(reply.getReply()));
	}
}
