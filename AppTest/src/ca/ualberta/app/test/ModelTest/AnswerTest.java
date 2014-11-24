package ca.ualberta.app.test.ModelTest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class AnswerTest extends TestCase {
	//test Answer constructor
		public void testAnswer() {
			String content= "A content";
			String username = "A username";
	            String imageString= "imageString";
	            
	            ArrayList<Reply>  reply= new  ArrayList<Reply>();
	            Long upvoteCount_answer=(long) 0;  
	            long ID_answer = new Date().getTime() - 100;
	            Map<String,Author> upvotedPerson = new HashMap<String, Author>();

	            Answer answer=new Answer(content, username, imageString);
	            
	            assertNotNull(answer);
	            assertEquals(answer.getContent(), "A content");
	            assertEquals(answer.getAuthor(), "A username");
	            assertEquals( new String(answer.getImage()),"imageString"); 
                assertEquals(answer.getReplyList(), reply);
	            assertEquals(answer.getAnswerUpvoteCount(),(long) 0);
               
	            
}
		public void testAddReply() {
			String content= "A content";
			String username = "A username";
	            String imgeString= "imageString";
	            
	            ArrayList<Reply>  reply= new  ArrayList<Reply>();
	            Long upvoteCount_answer=(long) 0;  
	            Long ID_answer = new Date().getTime() - 100;
	            Map<String,Author> upvotedPerson = new HashMap<String, Author>();

	            Answer answer=new Answer(content, username, "imageString");
	            Reply replyT=new Reply(content,username);
	            answer.addReply( replyT);
	            assertSame(answer.getReplyList().get(0),replyT);
	  } 

	       public void testGetReplyPosition() {
			String content= "A content";
			String username = "A username";
	            String imageString= "imageString";
	            
	            ArrayList<Reply>  reply= new  ArrayList<Reply>();
	            Long upvoteCount_answer=(long) 0;  
	            Long ID_answer = new Date().getTime() - 100;
	            Map<String,Author> upvotedPerson = new HashMap<String, Author>();

	            Answer answer=new Answer(content, username, imageString);
	            Reply replyT=new Reply(content,username);
	            answer.addReply( replyT);
	            assertEquals(answer.getReplyPosition(replyT),0);
	  } 

	      public void testSetUpvotedCount() {
			String content= "A content";
			String username = "A username";
	            String imgeString= "imageString";
	            
	            ArrayList<Reply>  reply= new  ArrayList<Reply>();
	            Long upvoteCount_answer=(long) 0;  
	            Long ID_answer = new Date().getTime() - 100;
	            Map<String,Author> upvotedPerson = new HashMap<String, Author>();

	            Answer answer=new Answer(content, username, "imageString");
	            Long newUpvoteCount_answer=(long) 10;  
	            answer.setUpvoteCount(newUpvoteCount_answer );

	            assertEquals(answer.getAnswerUpvoteCount(), 10);
	  } }