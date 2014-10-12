package ca.ualberta.app.models;

import java.io.File;
import java.util.Date;

/**
 * @author  bicheng
 */
public abstract class AuthorInputs {
	/**
	 * @uml.property  name="content"
	 */
	String content;
	File image;
	String author;
	/**
	 * @uml.property  name="timestamp"
	 */
	Date timestamp;
	long upvoteCount;
	long downvoteCount;
	/**
	 * @uml.property  name="replyList"
	 * @uml.associationEnd  
	 */
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

	/**
	 * @return
	 * @uml.property  name="content"
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return
	 * @uml.property  name="timestamp"
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 * @uml.property  name="timestamp"
	 */
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
