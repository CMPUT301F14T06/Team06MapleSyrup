package ca.ualberta.app.test;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.app.Reply;
import ca.ualberta.app.ReplyList;
import junit.framework.TestCase;

public class ReplyListTest extends TestCase {
	public void testReplyList(){
		ReplyList replyList = new ReplyList();
		List<Reply> repList = replyList.getList();
		ArrayList<Reply> repArrayList = replyList.getArrayList();
		assertTrue("Empty Answer List", repList.size() == 0);
		assertTrue("Empty Answer Array List", repArrayList.size() == 0);
	}
	
	public void testaddAnswer(){
		String answerString = "A answer";
		Reply answer = new Reply(answerString);
		ReplyList answerList = new ReplyList();
		answerList.addReply(answer);
		ArrayList<Reply> ansArrayList = answerList.getArrayList();
		assertTrue("Answer List Size", ansArrayList.size() == 1);
		assertTrue("Answer List contains answer", ansArrayList.contains(answer));
	}
	
	public void testremoveAnswer(){
		String answerString = "A answer";
		Reply answer = new Reply(answerString);
		ReplyList answerList = new ReplyList();
		answerList.addReply(answer);
		answerList.removeReply(0);
		ArrayList<Reply> ansArrayList = answerList.getArrayList();
		assertTrue("Answer List Size", ansArrayList.size() == 0);
	}
}
