package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class CalculateTotalScoreTest extends TestCase {
	public void testUpdateTotalScoreOnline(){
		String questionString = "A Question";
		String answerString = "A Answer";
		String userName = "userName";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		Answer answer1 = new Answer(answerString, userName, image);
		Answer answer2 = new Answer(answerString, userName, image);
		Answer answer3 = new Answer(answerString, userName, image);
		
		QuestionListManager questionListManager = new QuestionListManager();
		
		answer1.upvoteAnswer();
		answer1.upvoteAnswer();
		answer2.upvoteAnswer();
		
		question.addAnswer(answer1);
		question.addAnswer(answer2);
		question.addAnswer(answer3);
		
		questionListManager.addQuestion(question);
		Question targetQuestion = questionListManager.getQuestion(question.getID());

		assertEquals(3, targetQuestion.getTotalScore());
		
		questionListManager.deleteQuestion(question.getID());
	}
}
