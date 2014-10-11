package ca.ualberta.app;

import java.util.ArrayList;
import java.util.List;

public class AnswerList {
	ArrayList<Answer> answerList;

	public AnswerList() {
		answerList = new ArrayList<Answer>();
	}

	// return a List
	public List<Answer> getList() {
		return answerList;
	}

	// return an ArrayList
	public ArrayList<Answer> getArrayList() {
		
		return answerList;
	}

	public void addAnswer(Answer newAnswer) {
		this.answerList.add(newAnswer);
	}

	public void removeAnswer(int position) {
		answerList.remove(position);
	}

	public int size() {
		return answerList.size();
	}

	// get the item at current position
	public Answer getAnswer(int position) {
		return answerList.get(position);
	}

	public ReplyList getReplys(int position) {
		return answerList.get(position).replyList;
	}
}
