package ca.ualberta.app.test.ModelTest;

import java.util.ArrayList;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class AnswerTest extends TestCase {
	// test Answer constructor
	public void testAnswer() {
		String content = "A content";
		long userId = 123456789;
		String imageString = "imageString";

		ArrayList<Reply> reply = new ArrayList<Reply>();
		Answer answer = new Answer(content, userId, imageString);

		assertNotNull(answer);
		assertEquals(answer.getContent(), "A content");
		assertEquals(new String(answer.getImage()), "imageString");
		assertEquals(answer.getReplyList(), reply);
		assertEquals(answer.getAnswerUpvoteCount(), (long) 0);

	}

	public void testAddReply() {
		String content = "A content";
		long userId = 123456789;
		String imgeString = "imageString";

		Answer answer = new Answer(content, userId, imgeString);
		Reply replyT = new Reply(content, userId);
		answer.addReply(replyT);
		assertSame(answer.getReplyList().get(0), replyT);
	}

	public void testGetReplyPosition() {
		String content = "A content";
		long userId = 123456789;
		String imageString = "imageString";

		Answer answer = new Answer(content, userId, imageString);
		Reply replyT = new Reply(content, userId);
		answer.addReply(replyT);
		assertEquals(answer.getReplyPosition(replyT), 0);
	}

	public void testSetUpvotedCount() {
		String content = "A content";
		long userId = 123456789;
		String imgeString = "imageString";
		
		Answer answer = new Answer(content, userId, imgeString);
		Long newUpvoteCount_answer = (long) 10;
		answer.setUpvoteCount(newUpvoteCount_answer);

		assertEquals(answer.getAnswerUpvoteCount(), 10);
	}
}