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

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * This class will handle all kinds of user input
 */
public abstract class InputsModel {
	public String title;
	public String content;
	public Long userId;
	String imageString = null;
	Date timestamp;

	/**
	 * Question contains replyList and answerList
	 * 
	 * @param content
	 *            The content.
	 * @param userName
	 *            The ID of the user.
	 * @param title
	 *            The title of the question.
	 * @param image
	 *            The image in the question.
	 */
	public InputsModel(String content, Long userId, String title,
			String imageString) {
		this.timestamp = new Date();
		this.imageString = imageString;
		this.title = title;
		this.content = content;
		this.userId = userId;
	}

	/**
	 * Answer contain replyList
	 * 
	 * @param content
	 *            The content.
	 * @param userName
	 *            The ID of the user.
	 * @param image
	 *            The image in the question.
	 */
	public InputsModel(String content, Long userId, String imageString) {
		timestamp = new Date();
		this.imageString = imageString;
		this.content = content;
		this.userId = userId;
	}

	/**
	 * The reply
	 * 
	 * @param content
	 *            The content.
	 * @param userName
	 *            The ID of the user.
	 */
	public InputsModel(String content, Long userId) {
		timestamp = new Date();
		this.content = content;
		this.userId = userId;
	}

	/**
	 * Check if the question/answer has an image
	 * 
	 * @return true if the question/answer has an image, otherwise, return
	 *         false.
	 */
	public boolean hasImage() {
		return imageString != null;
	}

	/**
	 * The image of the question/answer
	 * 
	 * @param image
	 *            The image of the question/answer.
	 */
	public void setImage(byte[] imageByteArray) {
		try {
			this.imageString = new String(imageByteArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Return the image
	 * 
	 * @return the image.
	 */
	public byte[] getImage() {
		byte[] imageByteArray = null;
		try {
			imageByteArray = imageString.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return imageByteArray;
	}

	/**
	 * Edit the content of a question/answer
	 * 
	 * @param newContent
	 *            The new content.
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
	 * @param timestamp
	 *            the new date and time.
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Return the user name of an author
	 * 
	 * @return userName The user name of the author.
	 */
	public Long getUserId() {
		return userId;
	}
}
