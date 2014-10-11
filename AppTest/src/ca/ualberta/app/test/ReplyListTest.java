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
		assertTrue("Empty Reply List", repList.size() == 0);
		assertTrue("Empty Reply Array List", repArrayList.size() == 0);
	}
	
	public void testaddReply(){
		String replyString = "A Reply";
		Reply reply = new Reply(replyString);
		ReplyList replyList = new ReplyList();
		replyList.addReply(reply);
		ArrayList<Reply> repArrayList = replyList.getArrayList();
		assertTrue("Reply List Size", repArrayList.size() == 1);
		assertTrue("Reply List contains reply", repArrayList.contains(reply));
	}
	
	public void testremoveReply(){
		String replyString = "A reply";
		Reply reply = new Reply(replyString);
		ReplyList replyList = new ReplyList();
		replyList.addReply(reply);
		replyList.removeReply(0);
		ArrayList<Reply> repArrayList = replyList.getArrayList();
		assertTrue("Reply List Size", repArrayList.size() == 0);
	}
}
