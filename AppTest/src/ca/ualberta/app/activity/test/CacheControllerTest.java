package ca.ualberta.app.activity.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;

public class CacheControllerTest extends AndroidTestCase {
	public void testAddFavQuestion() {
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.addFavQuestions(mcontext, question);

		assertTrue("didnt's add question as fav",
				cacheController.hasFavorited(mcontext, question));

	}

	public void testGetFavoriteQuestionList(){
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		QuestionList qList = cacheController.getFavoriteQuestionList();
		assertNotNull(qList);
	}
}
