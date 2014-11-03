package ca.ualberta.app.activity.test;

import java.util.ArrayList;

import android.graphics.Bitmap;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionListManager;
import junit.framework.TestCase;

public class ViewAnswersTest extends TestCase {
	public void testViewAnswersOffline() {
		String questionString = "A Question";
		String answerString = "A Answer";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Answer answer = new Answer(answerString, userName, image);
		
		QuestionListController inputsListController = new QuestionListController();
		question.addAnswer(answer);
		inputsListController.addQuestion(question);
		
		ArrayList<Answer> answerList = inputsListController.getAnswers(0);
		Answer result = answerList.get(0);
		
		assertTrue("User cannot view answers", answerList.size() == 1);
		assertEquals(answer.getContent(), result.getContent());
		assertEquals(answer.getAuthor(), result.getAuthor());
	}
	
	public void testViewAnswersOffline_extremeCase(){
		String questionString = "A Question";
		String answerString = "A Answer";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Answer answer = new Answer(answerString, userName, image);
		
		QuestionListController inputsListController = new QuestionListController();
		question.addAnswer(answer);
		inputsListController.addQuestion(question);
		
		int position_q = inputsListController.getQuestionPosition(question);
		int position_a = inputsListController.getAnswerPosition(answer,position_q);
		
		ArrayList<Answer> answerList = inputsListController.getAnswers(position_q);
		Answer result = answerList.get(position_a);
		
		assertTrue("User cannot view answers", answerList.size() == 1);
		assertEquals(answer.getContent(), result.getContent());
		assertEquals(answer.getAuthor(), result.getAuthor());
	}
	
	public void testViewAnswerOnline(){
		String questionString = "A Question";
		String answerString = "A Answer";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Answer answer = new Answer(answerString, userName, image);
		QuestionListManager questionListManager = new QuestionListManager();
		question.addAnswer(answer);
		questionListManager.addQuestion(question);
		Question targetQuestion = questionListManager.getQuestion(question.getID());
		
		ArrayList<Answer> answerList = targetQuestion.getAnswers();
		int position = targetQuestion.getAnswerPosition(answer);
		Answer result = answerList.get(position);
		
		assertTrue("User cannot view answers", answerList.size() == 1);
		assertEquals(answer.getContent(), result.getContent());
		assertEquals(answer.getAuthor(), result.getAuthor());
		
		questionListManager.deleteQuestion(question.getID());
	}
}
