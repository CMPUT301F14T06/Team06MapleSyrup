/*
 * Copyright 2014 Anni Dai
 * Copyright 2014 Bicheng Yan
 * Copyright 2014 Liwen Chen
 * Copyright 2014 Liang Jingjing
 * Copyright 2014 Xiaocong Zhou
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.app.models;

import java.util.Date;

import android.graphics.Bitmap;

/**
 * This class will handle all kinds of user input
 * 
 * @author Anni
 *
 */
public abstract class InputsModel {
	public String title;
	public String content;
	public String userName;
	Bitmap image = null;
	Date timestamp;
	
	/**
	 * Question contains replyList and answerList
	 * 
	 * @param content The content.
	 * @param userName The ID of the user.
	 * @param title The title of the question.
	 * @param image The image in the question.
	 */
	public InputsModel(String content, String userName, String title,  
			Bitmap image) {
		this.timestamp = new Date();
		this.image = image;
		this.title = title;
		this.content = content;
		this.userName = userName;
	}

	/**
	 * Answer contain replyList
	 * 
	 * @param content The content.
	 * @param userName The ID of the user.
	 * @param image The image in the question.
	 */
	public InputsModel(String content, String userName, Bitmap image) {
		timestamp = new Date();
		this.image = image;
		this.content = content;
		this.userName = userName;
	}

	/**
	 * The reply
	 * 
	 * @param content The content.
	 * @param userName The ID of the user.
	 */
	public InputsModel(String content, String userName) {
		timestamp = new Date();
		this.content = content;
		this.userName = userName;
	}

	/**
	 * Check if the question/answer has an image
	 * 
	 * @return true if the question/answer has an image, otherwise, return false.
	 */
	public boolean hasImage() {
		return image != null;
	}

	/**
	 * The image of the question/answer
	 * 
	 * @param image The image of the question/answer.
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}

	/**
	 * Return the image
	 * 
	 * @return the image.
	 */
	public Bitmap getImage() {
		return image;
	}

	/**
	 * Edit the content of a question/answer
	 * 
	 * @param newContent The new content.
	 */
	public void editContent(String newContent) {
		this.content = newContent;
	}

	/**
	 * Return the content of a question/answer
	 * 
	 * @return The content of a question/answer.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Return date and time
	 * 
	 * @return timestamp, date and time.
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Set date and time
	 * 
	 * @param timestamp the new date and time.
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Return the user name of an author
	 * 
	 * @return userName The user name of the author.
	 */
	public String getAuthor() {
		return userName;
	}
}
