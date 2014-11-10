package ca.ualberta.app.activity.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;

public class CacheControllerTest extends AndroidTestCase {
	
	String questionString = "A Question";
	String userName = "userName";
	String titleString = "title";
	Bitmap image = null;
	Question question = new Question(questionString, userName, titleString,
			image);
	
	public void testAddFavQuestion() {
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
	
	public void testRemoveFavQuestion(){
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.removeFavQuestions(mcontext, question);
		
		assertTrue("didnt's remove fav question",
				!(cacheController.hasFavorited(mcontext, question)));
	}
	
	public void testAddLocalQuestion(){
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.addLocalQuestions(mcontext, question);

		assertTrue("didnt's add question as Loacl",
				cacheController.hasSaved(mcontext, question));
	}
	
	public void testGetLocalQuestionsList(){
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		QuestionList qList = cacheController.getLocalQuestionsList();
		assertNotNull(qList);
	}
	
	public void testRemoveLocalQuestion(){
		Context mcontext = getContext();
		CacheController cacheController = new CacheController(mcontext);
		cacheController.removeLocalQuestions(mcontext, question);
		
		assertTrue("didnt's remove local question",
				!(cacheController.hasFavorited(mcontext, question)));
	}
}
