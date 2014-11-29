package ca.ualberta.app.test.ControllerTest;

import android.content.Context;
import android.test.AndroidTestCase;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;

public class CacheControllerTest extends AndroidTestCase {
	// Create a Question object
	String questionString = "A Question";
	Long userId = (long) 123;
	String titleString = "title";
	String imageByteArray = null;
	Question question = new Question(questionString, userId, titleString,
			imageByteArray);

	public void testAddFavQuestion() {
		// add the Question as a Favorite Question
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.addFavQuestion(mcontext, question);

		// check the result
		assertTrue("didnt's add question as fav",
				cacheController.hasFavorited(mcontext, question));

	}

	public void testGetFavoriteQuestionList() {
		// get the Question from the Favorite QuestionList
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);

		// check the result
		QuestionList qList = cacheController.getFavoriteQuestionList(mcontext);
		assertNotNull(qList);
	}

	public void testRemoveFavQuestion() {
		// remove the Question from Favorite QuestionList
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.removeFavQuestions(mcontext, question);

		// check the result
		assertTrue("didnt's remove fav question",
				!(cacheController.hasFavorited(mcontext, question)));
	}

	public void testAddLocalQuestion() {
		// add the Question as a Local Cached Question
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.addLocalQuestion(mcontext, question);

		// check the result
		assertTrue("didnt's add question as Loacl",
				cacheController.hasSaved(mcontext, question));
	}

	public void testGetLocalQuestionsList() {
		// get the Question from Local QuestionList
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);

		// check the result
		QuestionList qList = cacheController.getLocalQuestionsList(mcontext);
		assertNotNull(qList);
	}

	public void testRemoveLocalQuestion() {
		// remove the Question from the Local Cached QuestionList
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.removeLocalQuestions(mcontext, question);

		// check the result
		assertTrue("didnt's remove local question",
				!(cacheController.hasFavorited(mcontext, question)));
	}
}
