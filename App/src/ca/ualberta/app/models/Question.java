package ca.ualberta.app.models;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Question extends InputsModel {
	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;
	Boolean selected = false;

	public Question(String content, String userName, String title, Bitmap image) {
		super(content, userName, title, image);
		replyList = new ArrayList<Reply>();
		answerList = new ArrayList<Answer>();
	}

	public void addReply(Reply newReply) {
		replyList.add(newReply);
	}

	public void addAnswer(Answer newAnswer) {
		answerList.add(newAnswer);
		answerCount = answerList.size();
	}

	public boolean getStatus() {
		return this.selected;
	}

	public void select() {
		this.selected = true;
	}

	public void unSelect() {
		this.selected = false;
	}

}
