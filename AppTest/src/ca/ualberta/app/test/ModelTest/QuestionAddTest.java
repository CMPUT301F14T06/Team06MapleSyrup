//package ca.ualberta.app.test.ModelTest;
//
//import java.util.ArrayList;
//import java.util.Date;
//
//import ca.ualberta.app.models.Answer;
//import ca.ualberta.app.models.Author;
//import ca.ualberta.app.models.Question;
//import ca.ualberta.app.models.Reply;
//import junit.framework.TestCase;
//
//public class QuestionAddTest extends TestCase {
//	// test initial Question object
//	public void testQuestion() {
//		String username = "username";
//		String questionString = "A question";
//		String titleString = "A title";
//		String imageByteArray = null;
//		Author author = new Author(username);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//		assertEquals(questionString, question.getContent());
//	}
//
//	public void testAddAnswer() {
//		// create a Question and an Answer object
//		String username = "username";
//		String questionString = "A qustion";
//		String answerString = "A answer";
//		String titleString = "A title";
//		String imageByteArray = null;
//		Author author = new Author(username);
//		Answer answer = new Answer(answerString, author.getUsername(),
//				imageByteArray);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//
//		// add the answer to Question
//		question.addAnswer(answer);
//
//		// check the result
//		assertTrue("Answer is not equal",
//				answerString.equals(answer.getContent()));
//	}
//
//	public void testAddReply() {
//		// create a Question and a Reply Object
//		String username = "username";
//		String questionString = "A qustion";
//		String replyString = "A reply";
//		String titleString = "A title";
//		String imageByteArray = null;
//		Author author = new Author(username);
//		Reply reply = new Reply(replyString, author.getUsername());
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//
//		// add the reply to the Question
//		question.addReply(reply);
//
//		// check the result
//		assertTrue("Reply is not equal", replyString.equals(reply.getContent()));
//	}
//
//	public void testGetTitle() {
//		// create a Question
//		String username = "username";
//		String questionString = "A qustion";
//		String titleString = "A title";
//		String imageByteArray = null;
//		Author author = new Author(username);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//
//		assertEquals(question.getTitle(), "A title");
//	}
//
//	public void testGetID_question() {
//		// create a Question
//		String username = "username";
//		String questionString = "A qustion";
//		String titleString = "A title";
//		String imageByteArray = null;
//
//		long ID_question = new Date().getTime();;
//
//		Author author = new Author(username);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//		assertEquals(question.getID(), ID_question);
//	}
//
//	public void testSetID() {
//		// create a Question
//		String username = "username";
//		String questionString = "A qustion";
//		String titleString = "A title";
//		String imageByteArray = null;
//		
//		Long newID = (long) 100;
//
//		Author author = new Author(username);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//		question.setID(newID);
//		assertSame(question.getID(), newID);
//	}
//
//	public void testGetAnswerCount() {
//		// create a Question
//		String username = "username";
//		String questionString = "A qustion";
//		String titleString = "A title";
//		String imageByteArray = null;
//		String answerString = new String();
//
//		Author author = new Author(username);
//		Answer answer = new Answer(answerString, author.getUsername(),
//				imageByteArray);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//		question.addAnswer(answer);
//		assertEquals(question.getAnswerCount(), 1);
//	}
//
//	public void testGetAnswers() {
//		// create a Question
//		String username = "username";
//		String questionString = "A qustion";
//		String titleString = "A title";
//		String imageByteArray = null;
//		String answerString = new String();
//
//		ArrayList<Answer> answerList = new ArrayList<Answer>();
//
//		Author author = new Author(username);
//		Answer answer = new Answer(answerString, author.getUsername(),
//				imageByteArray);
//		Question question = new Question(questionString, author.getUsername(),
//				titleString, imageByteArray);
//		question.addAnswer(answer);
//
//		answerList.add(answer);
//
//		assertEquals(question.getAnswers(), answerList);
//	}
//}
