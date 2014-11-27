package ca.ualberta.app.controller;

import java.util.ArrayList;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.QuestionList;

public class AnswerListController {

	private ArrayList<Answer> AnswerList;

	public ArrayList<Answer> getAnswerList() {
		if (AnswerList == null) {
			AnswerList = new ArrayList<Answer>();
		}
		return AnswerList;
	}

	public void addAnswer(Answer newAnswer) {
		getAnswerList().add(newAnswer);
	}

	public void removeAnswer(Answer newAnswer) {
		getAnswerList().remove(newAnswer);
	}

	public int size() {
		return getAnswerList().size();
	}

	public Answer getAnswer(int position) {
		return getAnswerList().get(position);
	}

	public void clear() {
		getAnswerList().clear();
	}

	public int getAnswerPosition(Answer answer) {
		return getAnswerList().indexOf(answer);
	}
	
	public void addAll(ArrayList<Answer> newanswerList) {
		getAnswerList().addAll(newanswerList);
	}
}
