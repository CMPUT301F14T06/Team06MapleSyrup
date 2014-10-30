package ca.ualberta.app.activity.test;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.InputsListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class InputsListTest extends TestCase {
	public void testQuestionList() {
		InputsListController inputListController = new InputsListController();
		List<Question> quesList = inputListController.getQuestionList().getList();
		ArrayList<Question> quesArrayList = inputListController.getQuestionList().getArrayList();
		assertTrue("Empty Question List", quesList.size() == 0);
		assertTrue("Empty Question Array List", quesArrayList.size() == 0);
	}

	public void testAddQuestion() {
		String questionString = "A Question";
		String userName = "userName";
		String titleString = "A title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		InputsListController inputListController = new InputsListController();
		inputListController.addQuestion(question);
		ArrayList<Question> quesArrayList = inputListController.getQuestionList().getArrayList();
		assertTrue("Question List Size", quesArrayList.size() == 1);
		assertTrue("Question List contains question",
				quesArrayList.contains(question));
	}

	public void testRemoveQuestion() {
		String questionString = "A question";
		String userName = "userName";
		String titleString = "A title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		InputsListController inputListController = new InputsListController();
		inputListController.addQuestion(question);
		inputListController.removeQuestion(0);
		ArrayList<Question> quesArrayList = inputListController.getQuestionList().getArrayList();
		assertTrue("Question List Size", quesArrayList.size() == 0);
	}
	
	public void testAddReplyToQ(){
		String questionString = "A question";
		String ReplyString = "A Reply";
		String userName = "userName";
		String titleString = "A title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString, image);
		Reply reply = new Reply(ReplyString, userName);
		InputsListController inputListController = new InputsListController();
		inputListController.addQuestion(question);
		inputListController.addReplyToQ(reply, 0);
		assertTrue("Cannot add reply to Question",inputListController.getReplys(0).contains(reply));
	}
	
	 public void testAddAnswerToQ(){
		String questionString = "A question";
		String AnswerString = "A answer";
		String userName = "userName";
		String titleString = "A title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString, image);
		Answer answer = new Answer(AnswerString, userName, image);
		InputsListController inputListController = new InputsListController();
		inputListController.addQuestion(question);
		inputListController.addAnswerToQ(answer, 0);
		assertTrue("Cannot add answer to Question",inputListController.getAnswers(0).contains(answer));
	 }
	 
	 public void testAddReplyToA(){
		String questionString = "A question";
		String AnswerString = "A answer";
		String ReplyString = "A Reply";
		String userName = "userName";
		String titleString = "A title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString, image);
		Answer answer = new Answer(AnswerString, userName, image);
		Reply reply = new Reply(ReplyString, userName);
		InputsListController inputListController = new InputsListController();
		inputListController.addQuestion(question);
		inputListController.addAnswerToQ(answer, 0);
		inputListController.addReplyToA(reply, 0, 0);
		assertTrue("Cannot add reply to answer",inputListController.getReplysOfAnswer(0, 0).contains(reply));
	 }
}
