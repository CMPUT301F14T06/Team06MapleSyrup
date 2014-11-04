package ca.ualberta.app.activity.test;
import java.util.ArrayList;

import android.graphics.Bitmap;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionListManager;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;


public class ViewReplysTest extends TestCase {
	public void testViewReplysOffline() {
		String questionString = "A Question";
		String answerString = "A Answer";
		String replyString = "A reply";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Answer answer = new Answer(answerString, userName, image);
		Reply reply_A = new Reply(replyString, userName);
		Reply reply_B = new Reply(replyString, userName);
		
		QuestionListController inputsListController = new QuestionListController();
		question.addReply(reply_A);
		answer.addReply(reply_B);
		question.addAnswer(answer);
		inputsListController.addQuestion(question);
		
		ArrayList<Reply> replyList_Q = inputsListController.getReplys(0);
		ArrayList<Reply> replyList_A = inputsListController.getReplysOfAnswer(0, 0);
		Reply result_A = replyList_Q.get(0);
		Reply result_B = replyList_A.get(0);
		
		assertTrue("User cannot view QuestionReplys", replyList_Q.size() == 1);
		assertTrue("User cannot view AnswerReplys", replyList_A.size() == 1);
		assertEquals(reply_A.getContent(), result_A.getContent());
		assertEquals(reply_A.getAuthor(), result_A.getAuthor());
		assertEquals(reply_B.getContent(), result_B.getContent());
		assertEquals(reply_B.getAuthor(), result_B.getAuthor());
	}
	
	public void testViewReplysOffline_extremeCase(){
		String questionString = "A Question";
		String answerString = "A Answer";
		String replyString = "A reply";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Answer answer = new Answer(answerString, userName, image);
		Reply reply_A = new Reply(replyString, userName);
		Reply reply_B = new Reply(replyString, userName);
		
		QuestionListController inputsListController = new QuestionListController();
		question.addReply(reply_A);
		answer.addReply(reply_B);
		question.addAnswer(answer);
		inputsListController.addQuestion(question);
		
		int position_q = inputsListController.getQuestionPosition(question);
		int position_a = inputsListController.getAnswerPosition(answer,position_q);
		int position_q_r = inputsListController.getReplyPosition(position_q,reply_A);
		int position_a_r = inputsListController.getReplyPositionOfAnswer(position_q,position_a,reply_B);
		
		ArrayList<Reply> replyList_Q = inputsListController.getReplys(position_q);
		ArrayList<Reply> replyList_A = inputsListController.getReplysOfAnswer(position_q, position_a);
		Reply result_A = replyList_Q.get(position_q_r);
		Reply result_B = replyList_A.get(position_a_r);
		
		assertTrue("User cannot view QuestionReplys", replyList_Q.size() == 1);
		assertTrue("User cannot view AnswerReplys", replyList_A.size() == 1);
		assertEquals(reply_A.getContent(), result_A.getContent());
		assertEquals(reply_A.getAuthor(), result_A.getAuthor());
		assertEquals(reply_B.getContent(), result_B.getContent());
		assertEquals(reply_B.getAuthor(), result_B.getAuthor());
	}
	
	public void testViewReplysOnline(){
//		String questionString = "A Question";
//		String answerString = "A Answer";
//		String replyString = "A reply";
//		String userName = "userName";
//		String titleString = "title";
//		Bitmap image = null;
//		Question question = new Question(questionString, userName, titleString,
//				image);
//		Answer answer = new Answer(answerString, userName, image);
//		Reply reply_A = new Reply(replyString, userName);
//		Reply reply_B = new Reply(replyString, userName);
//		
//		QuestionListManager questionListManager = new QuestionListManager();
//		question.addReply(reply_A);
//		answer.addReply(reply_B);
//		question.addAnswer(answer);
//		questionListManager.addQuestion(question);
//		Question targetQuestion = questionListManager.getQuestion(question.getID());
//		
//		int position_a = targetQuestion.getAnswerPosition(answer);
//		
//		ArrayList<Reply> replyList_Q = targetQuestion.getReplys();
//		ArrayList<Answer> answerList = targetQuestion.getAnswers();
//		Answer answerResult = answerList.get(position_a);
//		
//		int position_q_r = targetQuestion.getReplyPosition(reply_A);
//		int position_a_r = answerResult.getReplyPosition(reply_B);
//				
//		ArrayList<Reply> replyList_A = answerResult.getReplyArrayList();
//
//		Reply result_A = replyList_Q.get(position_q_r);
//		Reply result_B = replyList_A.get(position_a_r);
//		
//		assertTrue("User cannot view QuestionReplys", replyList_Q.size() == 1);
//		assertTrue("User cannot view AnswerReplys", replyList_A.size() == 1);
//		assertEquals(reply_A.getContent(), result_A.getContent());
//		assertEquals(reply_A.getAuthor(), result_A.getAuthor());
//		assertEquals(reply_B.getContent(), result_B.getContent());
//		assertEquals(reply_B.getAuthor(), result_B.getAuthor());
//		
//		questionListManager.deleteQuestion(question.getID());
	}
	
}
