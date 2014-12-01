package ca.ualberta.app.test.ModelTest;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class QuestionListTest extends TestCase {
	public void testQuestionList() {
		// test initial QuestionList
		QuestionListController inputListController = new QuestionListController();
		List<Question> quesList = inputListController.getQuestionList()
				.getList();
		ArrayList<Question> quesArrayList = inputListController
				.getQuestionList().getArrayList();
		assertTrue("Empty Question List", quesList.size() == 0);
		assertTrue("Empty Question Array List", quesArrayList.size() == 0);
	}

	public void testAddQuestion() {
		// create a Question object
		String questionString = "A Question";
		long userId = 123456789;
		String titleString = "A title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);

		// add the Question through Controller
		QuestionListController inputListController = new QuestionListController();
		inputListController.addQuestion(question);

		// check the result
		ArrayList<Question> quesArrayList = inputListController
				.getQuestionList().getArrayList();
		assertTrue("Question List Size", quesArrayList.size() == 1);
		assertTrue("Question List contains question",
				quesArrayList.contains(question));
	}

	public void testRemoveQuestion() {
		// create a Question object
		String questionString = "A question";
		long userId = 123456789;
		String titleString = "A title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);

		// remove the Question from the List through the Controller
		QuestionListController inputListController = new QuestionListController();
		inputListController.addQuestion(question);
		inputListController.removeQuestion(0);

		// check the result
		ArrayList<Question> quesArrayList = inputListController
				.getQuestionList().getArrayList();
		assertTrue("Question List Size", quesArrayList.size() == 0);
	}

	public void testAddReplyToQ() {
		// create a Question and a Reply object
		String questionString = "A question";
		String ReplyString = "A Reply";
		long userId = 123456789;
		String titleString = "A title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);
		Reply reply = new Reply(ReplyString, userId);

		QuestionListController inputListController = new QuestionListController();
		// add the Question to the List
		inputListController.addQuestion(question);
		// add the Reply to the Question
		inputListController.addReplyToQ(reply, 0);
		// check the result
		assertTrue("Cannot add reply to Question", inputListController
				.getReplys(0).contains(reply));
	}

	public void testAddAnswerToQ() {
		// create a Question and an answer object
		String questionString = "A question";
		String AnswerString = "A answer";
		long userId = 123456789;
		String titleString = "A title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);
		Answer answer = new Answer(AnswerString, userId, imageByteArray);

		QuestionListController inputListController = new QuestionListController();
		// add the Question to the List
		inputListController.addQuestion(question);
		// add the Answer to the Question
		inputListController.addAnswerToQ(answer, 0);
		// check the result
		assertTrue("Cannot add answer to Question", inputListController
				.getAnswers(0).contains(answer));
	}

	public void testAddReplyToA() {
		// create a Question, an answer and a Reply object
		String questionString = "A question";
		String AnswerString = "A answer";
		String ReplyString = "A Reply";
		long userId = 123456789;
		String titleString = "A title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);
		Answer answer = new Answer(AnswerString, userId, imageByteArray);
		Reply reply = new Reply(ReplyString, userId);

		QuestionListController inputListController = new QuestionListController();
		// add the Question to List
		inputListController.addQuestion(question);
		// add the Answer to Question
		inputListController.addAnswerToQ(answer, 0);
		// add the Reply to Answer
		inputListController.addReplyToA(reply, 0, 0);
		// check the result
		assertTrue("Cannot add reply to answer", inputListController
				.getReplysOfAnswer(0, 0).contains(reply));
	}
}
