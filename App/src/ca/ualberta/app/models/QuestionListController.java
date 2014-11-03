package ca.ualberta.app.models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.app.models.QuestionList;

public class QuestionListController {

	private QuestionList questionList;

	public QuestionList getQuestionList() {
		if (questionList == null) {
			questionList = new QuestionList();
		}
		return questionList;
	}
	public ArrayList<Question> getQuestionArrayList() {
		return getQuestionList().getArrayList();
	}
	public void addQuestion(Question newQuestion) {
		getQuestionList().addQuestion(newQuestion);
	}

	public void removeQuestion(int position) {
		getQuestionList().removeQuestion(position);
	}

	public int size() {
		return getQuestionList().size();
	}

	public Question getQuestion(int position) {
		return getQuestionList().getQuestion(position);
	}

	public void addReplyToQ(Reply newReply, int position) {
		getQuestionList().addReplyToQ(newReply, position);
	}

	public void addReplyToA(Reply newReply, int q_position, int a_position) {
		getQuestionList().addReplyToA(newReply, q_position, a_position);
	}

	public void addAnswerToQ(Answer newAnswer, int position) {
		getQuestionList().addAnswerToQ(newAnswer, position);
	}

	public ArrayList<Answer> getAnswers(int position) {
		return getQuestionList().getAnswers(position);
	}

	public ArrayList<Reply> getReplys(int position) {
		return getQuestionList().getReplys(position);
	}

	public ArrayList<Reply> getReplysOfAnswer(int q_position, int a_position) {
		return getQuestionList().getReplysOfAnswer(q_position, a_position);
	}

	public List<Answer> getAnswerList(int position) {
		return getQuestionList().getAnswerList(position);
	}

	public List<Reply> getReplyList(int position) {
		return getQuestionList().getReplyList(position);
	}

	public QuestionList searchQuestion(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public QuestionList searchAnswer(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void clear() {
		getQuestionList().getList().clear();
	}

	public void addAll(QuestionList searchQuestions) {
		getQuestionList().getList().addAll(searchQuestions.getList());
	}

	public int getQuestionPosition(Question question) {
		return getQuestionList().getArrayList().indexOf(question);
	}
	
	public int getAnswerPosition(Answer answer, int position_q) {
		return getAnswers(position_q).indexOf(answer);
	}
	public static QuestionList loadFromFile(Context context, String FILENAME) {
		QuestionList questionList = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<QuestionList>() {
			}.getType();
			questionList = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionList == null)
			return questionList = new QuestionList();
		return questionList;
	}

	public static void saveInFile(Context context, QuestionList questionList,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(questionList, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
