package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

public abstract class InputsModel {
	String title;
	String content;
	String authorLoginName;
	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;
	ArrayList<Bitmap> imageList;
	Date timestamp;
	long answerCount;
	long upvoteCount;
	long downvoteCount;
	long score;

	// Question
	public InputsModel(String content, String authorLoginName, String title,
			ArrayList<Bitmap> imageList) {
		replyList = new ArrayList<Reply>();
		answerList = new ArrayList<Answer>();
		this.imageList = new ArrayList<Bitmap>();
		this.imageList = imageList;
		timestamp = new Date();
		this.content = content;
		this.authorLoginName = authorLoginName;
		answerCount = 0;
		upvoteCount = 0;
		downvoteCount = 0;
	}

	// Answer
	public InputsModel(String content, String authorLoginName,
			ArrayList<Bitmap> imageList) {
		replyList = new ArrayList<Reply>();
		this.imageList = new ArrayList<Bitmap>();
		this.imageList = imageList;
		timestamp = new Date();
		this.content = content;
		this.authorLoginName = authorLoginName;
		answerCount = 0;
		upvoteCount = 0;
		downvoteCount = 0;
	}

	// Reply
	public InputsModel(String content, String authorLoginName) {
		timestamp = new Date();
		this.content = content;
		this.authorLoginName = authorLoginName;
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

	public void setImageList(ArrayList<Bitmap> imageList) {
		this.imageList = imageList;
	}

	public ArrayList<Bitmap> getImageList() {
		return imageList;
	}

	public boolean hasImageList() {
		return (imageList.size() != 0);
	}

	public void editContent(String newContent) {
		this.content = newContent;
	}

	public String getContent() {
		return content;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void upvote() {
		upvoteCount++;
	}

	public void downvote() {
		downvoteCount++;
	}
}
