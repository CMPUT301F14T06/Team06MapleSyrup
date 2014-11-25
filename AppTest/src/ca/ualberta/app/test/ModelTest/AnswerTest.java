package ca.ualberta.app.test.ModelTest;

import java.util.ArrayList;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class AnswerTest extends TestCase {
	// test Answer constructor
	public void testAnswer() {
		String content = "A content";
		String username = "A username";
		String imageString = "imageString";

		ArrayList<Reply> reply = new ArrayList<Reply>();
		Answer answer = new Answer(content, username, imageString);

		assertNotNull(answer);
		assertEquals(answer.getContent(), "A content");
		assertEquals(answer.getAuthor(), "A username");
		assertEquals(new String(answer.getImage()), "imageString");
		assertEquals(answer.getReplyList(), reply);
		assertEquals(answer.getAnswerUpvoteCount(), (long) 0);

	}

	public void testAddReply() {
		String content = "A content";
		String username = "A username";
		String imgeString = "imageString";

		Answer answer = new Answer(content, username, imgeString);
		Reply replyT = new Reply(content, username);
		answer.addReply(replyT);
		assertSame(answer.getReplyList().get(0), replyT);
	}

	public void testGetReplyPosition() {
		String content = "A content";
		String username = "A username";
		String imageString = "imageString";

		Answer answer = new Answer(content, username, imageString);
		Reply replyT = new Reply(content, username);
		answer.addReply(replyT);
		assertEquals(answer.getReplyPosition(replyT), 0);
	}

	public void testSetUpvotedCount() {
		String content = "A content";
		String username = "A username";
		String imgeString = "imageString";
		
		Answer answer = new Answer(content, username, imgeString);
		Long newUpvoteCount_answer = (long) 10;
		answer.setUpvoteCount(newUpvoteCount_answer);

		assertEquals(answer.getAnswerUpvoteCount(), 10);
	}
}