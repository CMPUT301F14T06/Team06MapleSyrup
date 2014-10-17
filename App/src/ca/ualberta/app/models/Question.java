package ca.ualberta.app.models;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Question extends InputsModel {
	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;

	public Question(String content, int userID, String title,
			Bitmap image) {
		super(content, userID, title, image);
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
	

}
