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

public abstract class InputsModel {
	public String title;
	public String content;
	public String userName;
	Bitmap image = null;
	Date timestamp;
	
	// Question contains replyList and answerList
	public InputsModel(String content, String userName, String title,  
			Bitmap image) {
		this.timestamp = new Date();
		this.image = image;
		this.title = title;
		this.content = content;
		this.userName = userName;
	}

	// Answer contain replyList
	public InputsModel(String content, String userName, Bitmap image) {
		timestamp = new Date();
		this.image = image;
		this.content = content;
		this.userName = userName;
	}

	// Reply
	public InputsModel(String content, String userName) {
		timestamp = new Date();
		this.content = content;
		this.userName = userName;
	}

	public boolean hasImage() {
		return image != null;
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

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getAuthor() {
		return userName;
	}
}
