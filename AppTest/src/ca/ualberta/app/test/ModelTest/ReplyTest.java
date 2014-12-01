package ca.ualberta.app.test.ModelTest;

import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class ReplyTest extends TestCase {
	public void testReply() {
		String content="A content";
		long userId = 123456789;

            Reply r= new Reply(content,userId);

		assertEquals(r.getContent(),"A content");
  
	}
}
