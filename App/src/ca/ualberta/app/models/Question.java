package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class Question extends InputsModel {
	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;
	ArrayList<Bitmap> imageList;

	public Question(String content, String authorLoginName, String title,
			ArrayList<Bitmap> imageList) {
		super(content, authorLoginName, title, imageList);
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

	public List<Reply> getReplyList() {
		return replyList;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}
}
