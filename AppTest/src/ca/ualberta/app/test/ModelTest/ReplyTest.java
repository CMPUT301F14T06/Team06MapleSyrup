package ca.ualberta.app.test.ModelTest;

import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class ReplyTest extends TestCase {
	public void testReply() {
		String content="A content";
		String userName="A userName";

            Reply r= new Reply(content,userName);

		assertEquals(r.getContent(),"A content");
        assertEquals(r.getAuthor(),"A userName");
  
	}
}
