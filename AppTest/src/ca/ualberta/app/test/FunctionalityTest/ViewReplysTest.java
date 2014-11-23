package ca.ualberta.app.test.FunctionalityTest;

import java.util.ArrayList;

import android.graphics.Bitmap;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class ViewReplysTest extends TestCase {
	public void testViewReplysOffline() {
		// Create a Question, an Answer, and Reply objects
		String questionString = "A Question";
		String answerString = "A Answer";
		String replyString = "A reply";
		String userName = "userName";
		String titleString = "title";
		String imageByteArray = null;
		Question question = new Question(questionString, userName, titleString,
				imageByteArray);
		Answer answer = new Answer(answerString, userName, imageByteArray);
		Reply reply_A = new Reply(replyString, userName);
		Reply reply_B = new Reply(replyString, userName);

		// add one Reply to Question and add the other Reply to Answer, then add
		// the Answer to the Question
		QuestionListController inputsListController = new QuestionListController();
		question.addReply(reply_A);
		answer.addReply(reply_B);
		question.addAnswer(answer);
		inputsListController.addQuestion(question);

		// get two ReplyList
		ArrayList<Reply> replyList_Q = inputsListController.getReplys(0);
		ArrayList<Reply> replyList_A = inputsListController.getReplysOfAnswer(
				0, 0);
		Reply result_A = replyList_Q.get(0);
		Reply result_B = replyList_A.get(0);

		// check the result
		assertTrue("User cannot view QuestionReplys", replyList_Q.size() == 1);
		assertTrue("User cannot view AnswerReplys", replyList_A.size() == 1);
		assertEquals(reply_A.getContent(), result_A.getContent());
		assertEquals(reply_A.getAuthor(), result_A.getAuthor());
		assertEquals(reply_B.getContent(), result_B.getContent());
		assertEquals(reply_B.getAuthor(), result_B.getAuthor());
	}

	public void testViewReplysOffline_extremeCase() {
		// Create a Question, an Answer, and Reply objects
		String questionString = "A Question";
		String answerString = "A Answer";
		String replyString = "A reply";
		String userName = "userName";
		String titleString = "title";
		String imageByteArray = null;
		Question question = new Question(questionString, userName, titleString,
				imageByteArray);
		Answer answer = new Answer(answerString, userName, imageByteArray);
		Reply reply_A = new Reply(replyString, userName);
		Reply reply_B = new Reply(replyString, userName);

		// add one Reply to Question and add the other Reply to Answer, then add
		// the Answer to the Question
		QuestionListController inputsListController = new QuestionListController();
		question.addReply(reply_A);
		answer.addReply(reply_B);
		question.addAnswer(answer);
		inputsListController.addQuestion(question);

		// get the position from their List
		int position_q = inputsListController.getQuestionPosition(question);
		int position_a = inputsListController.getAnswerPosition(answer,
				position_q);
		int position_q_r = inputsListController.getReplyPosition(position_q,
				reply_A);
		int position_a_r = inputsListController.getReplyPositionOfAnswer(
				position_q, position_a, reply_B);

		// get two ReplyList using the position
		ArrayList<Reply> replyList_Q = inputsListController
				.getReplys(position_q);
		ArrayList<Reply> replyList_A = inputsListController.getReplysOfAnswer(
				position_q, position_a);
		Reply result_A = replyList_Q.get(position_q_r);
		Reply result_B = replyList_A.get(position_a_r);

		// check the result
		assertTrue("User cannot view QuestionReplys", replyList_Q.size() == 1);
		assertTrue("User cannot view AnswerReplys", replyList_A.size() == 1);
		assertEquals(reply_A.getContent(), result_A.getContent());
		assertEquals(reply_A.getAuthor(), result_A.getAuthor());
		assertEquals(reply_B.getContent(), result_B.getContent());
		assertEquals(reply_B.getAuthor(), result_B.getAuthor());
	}

	public void testViewReplysOnline() {
//		// Create a Question, an Answer, and Reply objects
//		String questionString = "A Question";
//		String answerString = "A Answer";
//		String replyString = "A reply";
//		String userName = "userName";
//		String titleString = "title";
//		String imageByteArray = null;
//		Question question = new Question(questionString, userName, titleString,
//				imageByteArray);
//		Answer answer = new Answer(answerString, userName, imageByteArray);
//		Reply reply_A = new Reply(replyString, userName);
//		Reply reply_B = new Reply(replyString, userName);
//
//		// add one Reply to Question and add the other Reply to Answer, then add
//		// the Answer to the Question, then add the Quesion to server
//		QuestionListManager questionListManager = new QuestionListManager();
//		question.addReply(reply_A);
//		answer.addReply(reply_B);
//		question.addAnswer(answer);
//		questionListManager.addQuestion(question);
//		Question targetQuestion = questionListManager.getQuestion(question
//				.getID());
//
//		// get the ReplyLists of Quesiton
//		ArrayList<Reply> replyList_Q = targetQuestion.getReplys();
//		ArrayList<Answer> answerList = targetQuestion.getAnswers();
//		Answer answerResult = answerList.get(0);
//
//		// get the position of two Reply
//		int position_q_r = targetQuestion.getReplyPosition(reply_A);
//		int position_a_r = answerResult.getReplyPosition(reply_B);
//
//		// get the replyList of Answer
//		ArrayList<Reply> replyList_A = answerResult.getReplyArrayList();
//
//		// get the two Reply
//		Reply result_A = replyList_Q.get(position_q_r);
//		Reply result_B = replyList_A.get(position_a_r);
//
//		// check the result
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
