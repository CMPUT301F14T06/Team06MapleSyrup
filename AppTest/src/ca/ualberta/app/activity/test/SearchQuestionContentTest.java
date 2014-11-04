package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionListManager;
import junit.framework.TestCase;

public class SearchQuestionContentTest extends TestCase {
	public void testSearchQuestionContent() {
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question result = null;
		Question question = new Question(questionString, userName, titleString,image);
		
		QuestionListManager questionListManager = new QuestionListManager();
		QuestionListController questionListController = new QuestionListController();
		questionListManager.addQuestion(question);
		
		questionListController.clear();
		questionListController.addAll(questionListManager.searchQuestions(questionString, null));	
		
		if (questionListController.size() == 1){
			result = questionListController.getQuestion(0);
		}
		
		assertEquals(1, questionListController.size());
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
		
		questionListManager.deleteQuestion(question.getID());
	}
	
	public void testSearchQuestionTitle() {
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question result = null;
		Question question = new Question(questionString, userName, titleString,image);
		
		QuestionListManager questionListManager = new QuestionListManager();
		QuestionListController questionListController = new QuestionListController();
		questionListManager.addQuestion(question);
		
		questionListController.clear();
		questionListController.addAll(questionListManager.searchQuestions(titleString, null));	
		
		if (questionListController.size() == 1){
			result = questionListController.getQuestion(0);
		}
		
		assertEquals(1, questionListController.size());
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
		
		questionListManager.deleteQuestion(question.getID());
	}
	
	public void testSearchQuestionUserName() {
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question result = null;
		Question question = new Question(questionString, userName, titleString,image);
		
		QuestionListManager questionListManager = new QuestionListManager();
		QuestionListController questionListController = new QuestionListController();
		questionListManager.addQuestion(question);
		
		questionListController.clear();
		questionListController.addAll(questionListManager.searchQuestions(userName, null));	
		
		if (questionListController.size() == 1){
			result = questionListController.getQuestion(0);
		}
		
		assertEquals(1, questionListController.size());
		assertEquals(question.getContent(), result.getContent());
		assertEquals(question.getAuthor(), result.getAuthor());
		assertEquals(question.getTitle(), result.getTitle());
		assertEquals(question.getID(), result.getID());
		
		questionListManager.deleteQuestion(question.getID());
	}
}
