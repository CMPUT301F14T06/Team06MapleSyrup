package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class Answer extends InputsModel {
	ArrayList<Reply> replyList;

	public Answer(String content, String userName, Bitmap image) {
		super(content, userName, image);
		replyList = new ArrayList<Reply>();
	}

	public void addReply(Reply newReply) {
		replyList.add(newReply);
	}

	public List<Reply> getReplyList() {
		return replyList;
	}

	public ArrayList<Reply> getReplyArrayList() {
		// TODO Auto-generated method stub
		return replyList;
	}

	public int getReplyPosition(Reply reply) {
		// TODO Auto-generated method stub
		return replyList.indexOf(reply);
	}
}
