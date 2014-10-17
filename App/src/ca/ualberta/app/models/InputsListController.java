package ca.ualberta.app.models;


import java.util.ArrayList;
import java.util.List;

import ca.ualberta.app.models.InputsListModel;

public class InputsListController {
	
	InputsListModel inputsListModel = new InputsListModel();
	private  InputsListModel questionList = null;
	
	public InputsListModel getQuestionList(){
		if (questionList == null) {
			questionList = new InputsListModel();
		}
			return questionList;
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

	public List<Answer> getAnswerList(int position) {
		return getQuestionList().getAnswerList(position);
	}

	public List<Reply> getReplyList(int position) {
		return getQuestionList().getReplyList(position);
	}
	
}
