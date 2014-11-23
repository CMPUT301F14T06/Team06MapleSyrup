package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class CalculateTotalScoreTest extends TestCase {
//	//test the method of Calculate total score for a Question
//	public void testUpdateTotalScoreOnline(){
//		//create a Question object
//		String questionString = "A Question";
//		String answerString = "A Answer";
//		String userName = "userName";
//		String titleString = "title";
//		String imageByteArray = null;
//		Question question = new Question(questionString, userName, titleString,
//				imageByteArray);
//		
//		//crease three answer objects
//		Answer answer1 = new Answer(answerString, userName, imageByteArray);
//		Answer answer2 = new Answer(answerString, userName, imageByteArray);
//		Answer answer3 = new Answer(answerString, userName, imageByteArray);
//		
//		QuestionListManager questionListManager = new QuestionListManager();
//		
//		//upvote Answers
//		answer1.upvoteAnswer();
//		answer1.upvoteAnswer();
//		answer2.upvoteAnswer();
//		
//		//add these answers to the question
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		question.addAnswer(answer3);
//		
//		//add the Question to the server through Manager
//		questionListManager.addQuestion(question);
//		
//		//check the result
//		Question targetQuestion = questionListManager.getQuestion(question.getID());
//		assertEquals(3, targetQuestion.getTotalScore());
//		
//		questionListManager.deleteQuestion(question.getID());
//	}
}
