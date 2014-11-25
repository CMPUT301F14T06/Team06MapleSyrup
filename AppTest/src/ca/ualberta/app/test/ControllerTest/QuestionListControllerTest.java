package ca.ualberta.app.test.ControllerTest;

import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class QuestionListControllerTest extends TestCase {
	// create a Question and QuestionList Controller object
	String questionString = "A Question";
	String userName = "userName";
	String titleString = "title";
	String imageByteArray = null;
	Question q1 = new Question(questionString, userName, titleString,
			imageByteArray);
	Question q2 = new Question(questionString, userName, titleString,
			imageByteArray);
	// add the Question to the List
	QuestionListController qlc = new QuestionListController();

	public void testCreate() {
		QuestionListController qlc = new QuestionListController();
		assertTrue("question list controller should not be null", qlc != null);
	}

	public void testSize() {

		qlc.addQuestion(q1);
		assertTrue("size of question list should be 1", qlc.size() == 1);
	}

	public void testAddQuestion() {

		qlc.addQuestion(q1);

		assertTrue("no question in question list",
				qlc.getQuestionList().size() == 1);
		assertTrue("q1 was not added", qlc.getQuestionList().getArrayList()
				.contains(q1));
	}

	public void testRemoveQuestion() {

		qlc.addQuestion(q1);
		qlc.removeQuestion(0);
		assertTrue("size of question list should not be 0", qlc
				.getQuestionList().size() == 0);

	}

	public void testGetQuestionList() {
		qlc.addQuestion(q1);
		assertTrue("size of question list should not be 0", qlc
				.getQuestionList().size() != 0);

	}

	public void testAddReplyToQ() {
		String ReplyString = "A Reply";
		Reply r = new Reply(ReplyString, userName);
		// add Question
		qlc.addQuestion(q1);
		// add the Reply to the Question
		qlc.addReplyToQ(r, 0);
		assertTrue("no reply add to Question",
				qlc.getQuestionList().getReplys(0).contains(r));

	}

	public void testAddReplyToA() {
		String ReplyString = "A Reply";
		String AnswerString = "A answer";
		Answer a = new Answer(AnswerString, userName, imageByteArray);
		Reply r = new Reply(ReplyString, userName);
		// add Question
		qlc.addQuestion(q1);
		// add the Answer to Question
		qlc.addAnswerToQ(a, 0);
		// add the Reply to Answer
		qlc.addReplyToA(r, 0, 0);
		assertTrue("no reply add to answer", qlc.getQuestionList()
				.getReplysOfAnswer(0, 0).contains(r));
	}

	public void testAddAnswerToQ() {
		String AnswerString = "A answer";
		Answer a = new Answer(AnswerString, userName, imageByteArray);
		// add Question
		qlc.addQuestion(q1);
		// add the Answer to the Question
		qlc.addAnswerToQ(a, 0);
		// check the result
		assertTrue("no answer add to Question", qlc.getQuestionList()
				.getAnswers(0).contains(a));
	}

}
