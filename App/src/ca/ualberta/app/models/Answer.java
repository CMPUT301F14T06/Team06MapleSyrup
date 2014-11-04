package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

public class Answer extends InputsModel {
	ArrayList<Reply> replyList;
	long upvoteCount_answer;
	long ID_answer;

	public Answer(String content, String userName, Bitmap image) {
		super(content, userName, image);
		this.replyList = new ArrayList<Reply>();
		this.ID_answer = new Date().getTime() - 100;
		this.upvoteCount_answer = 0;
	}

	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	public List<Reply> getReplyList() {
		return this.replyList;
	}

	public ArrayList<Reply> getReplyArrayList() {
		// TODO Auto-generated method stub
		return this.replyList;
	}

	public int getReplyPosition(Reply reply) {
		// TODO Auto-generated method stub
		return this.replyList.indexOf(reply);
	}

	public void upvoteAnswer() {
		this.upvoteCount_answer++;
	}

	public long getAnswerUpvoteCount() {
		return this.upvoteCount_answer;
	}
}
