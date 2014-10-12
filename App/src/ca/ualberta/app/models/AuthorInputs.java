package ca.ualberta.app.models;

import java.io.File;
import java.util.Date;

public abstract class AuthorInputs {
	String content;
	File image;
	String author;
	Date timestamp;
	long upvoteCount;
	long downvoteCount;
	ReplyList replyList;

	public AuthorInputs(String content, String author) {
		replyList = new ReplyList();
		timestamp = new Date();
		this.content = content;
		this.author = author;
		upvoteCount = 0;
		downvoteCount = 0;

	}

	public void addReply(Reply newReply) {
		replyList.addReply(newReply);
	}

	public void editContent(String newContent) {
		this.content = newContent;
	}

	// public void editImage(File newImage) {
	// this.image = newImage;
	// }
	//
	// public void addImage() {
	//
	// }

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
