package ca.ualberta.app.models;

import java.util.Date;

import android.graphics.Bitmap;

public abstract class InputsModel {
	String title;
	String content;
	int userID;
	String userName;
	Bitmap image;
	Date timestamp;
	long answerCount;
	long upvoteCount;
	long downvoteCount;
	long score;

	// Question contains replyList and answerList
	public InputsModel(String content, int userID, String title, Bitmap image) {
		timestamp = new Date();
		this.image = image;
		this.content = content;
		this.userID = userID;
		//userName = getUserName();
		answerCount = 0;
		upvoteCount = 0;
		downvoteCount = 0;
	}

	// Answer contain replyList
	public InputsModel(String content, int userID, Bitmap image) {
		timestamp = new Date();
		this.image = image;
		this.content = content;
		this.userID = userID;
		answerCount = 0;
		upvoteCount = 0;
		downvoteCount = 0;
	}

	// Reply
	public InputsModel(String content, int userID) {
		timestamp = new Date();
		this.content = content;
		this.userID = userID;
	}

	public boolean hasImage() {
		return (image.getByteCount() != 0);
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public Bitmap getImage() {
		return image;
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

	public String getUserName() {
		return AuthorList.getUserName(userID);
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

	public long getUpvoteCount() {
		// TODO Auto-generated method stub
		return upvoteCount - downvoteCount;
	}
}
