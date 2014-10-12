package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.List;

public class QuestionList {
	ArrayList<Question> questionList;

	public QuestionList() {
		questionList = new ArrayList<Question>();
	}

	// return a List
	public List<Question> getList() {
		return questionList;
	}

	// return an ArrayList
	public ArrayList<Question> getArrayList() {

		return questionList;
	}

	public void addQuestion(Question newQuestion) {
		this.questionList.add(newQuestion);
	}

	public void removeQuestion(int position) {
		questionList.remove(position);
	}

	public int size() {
		return questionList.size();
	}

	// get the item at current position
	public Question getQuestion(int position) {
		return questionList.get(position);
	}

	public AnswerList getAnswers(int position) {
		return questionList.get(position).answerList;
	}

	public ReplyList getReplys(int position) {
		return questionList.get(position).replyList;
	}
}