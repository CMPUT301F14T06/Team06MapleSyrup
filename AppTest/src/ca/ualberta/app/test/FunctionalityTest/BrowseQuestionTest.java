package ca.ualberta.app.test.FunctionalityTest;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class BrowseQuestionTest extends TestCase {
	// test Browse Question offline
	public void testBrowseQuestionsOffline() {
		// create a Question object
		String questionString = "A Question";
		Long userId = (long) 123;
		String titleString = "title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);

		// add the question to the QuestionListController
		QuestionListController inputsListController = new QuestionListController();
		inputsListController.addQuestion(question);

		// check the result
		Question result = inputsListController.getQuestion(0);
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getUserId(), result.getUserId());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
	}

	public void testBrowseQuestionsOffline_extremeCase() {
		// create a Question object
		String questionString = "A Question";
		Long userId = (long) 123;
		String titleString = "title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);

		// add the question to the QuestionListController
		QuestionListController inputsListController = new QuestionListController();
		inputsListController.addQuestion(question);

		// get the Question position on the Controller
		int position = inputsListController.getQuestionPosition(question);
		Question result = inputsListController.getQuestion(position);

		// check the result
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getUserId(), result.getUserId());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
	}

	public void testBrowseQuestionsOnline() {
		// create a Question object
		String questionString = "A Question";
		Long userId = (long) 123;
		String titleString = "title";
		String imageByteArray = null;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);

		// add the Question to the server through Manager
		QuestionListManager questionListManager = new QuestionListManager();
		questionListManager.addQuestion(question);

		// check the result
		Question result = questionListManager.getQuestion(question.getID());
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getUserId(), result.getUserId());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());

		questionListManager.deleteQuestion(question.getID());
	}
}
