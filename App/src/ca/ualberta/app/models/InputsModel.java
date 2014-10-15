package ca.ualberta.app.models;

import java.io.File;
import java.util.Date;


public abstract class InputsModel {

	String content;
	File image;
	String authorLoginName;
	Date timestamp;
	long upvoteCount;
	long downvoteCount;

	public InputsModel(String content, String authorLoginName) {
		timestamp = new Date();
		this.content = content;
		this.authorLoginName = authorLoginName;
		upvoteCount = 0;
		downvoteCount = 0;

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
