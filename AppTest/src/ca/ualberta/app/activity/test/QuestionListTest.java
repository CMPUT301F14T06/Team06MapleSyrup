package ca.ualberta.app.activity.test;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import junit.framework.TestCase;

public class QuestionListTest extends TestCase {
	public void testQuestionList() {
		QuestionList questionList = new QuestionList();
		List<Question> quesList = questionList.getList();
		ArrayList<Question> quesArrayList = questionList.getArrayList();
		assertTrue("Empty Question List", quesList.size() == 0);
		assertTrue("Empty Question Array List", quesArrayList.size() == 0);
	}

	public void testaddQuestion() {
		String questionString = "A Question";
		String authorString = "An Author";
		String titleString = "A title";
		Question question = new Question(questionString, authorString,
				titleString);
		QuestionList questionList = new QuestionList();
		questionList.addQuestion(question);
		ArrayList<Question> quesArrayList = questionList.getArrayList();
		assertTrue("Question List Size", quesArrayList.size() == 1);
		assertTrue("Question List contains question",
				quesArrayList.contains(question));
	}

	public void testremoveQuestion() {
		String questionString = "A question";
		String authorString = "An Author";
		String titleString = "A title";
		Question question = new Question(questionString, authorString,
				titleString);
		QuestionList questionList = new QuestionList();
		questionList.addQuestion(question);
		questionList.removeQuestion(0);
		ArrayList<Question> quesArrayList = questionList.getArrayList();
		assertTrue("Question List Size", quesArrayList.size() == 0);
	}
}
