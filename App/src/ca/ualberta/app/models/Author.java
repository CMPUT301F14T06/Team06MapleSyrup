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

import java.util.ArrayList;

/**
 * This class contains all functionalities an author object should have.
 */
public class Author extends User {
	// final static boolean loginState = true;

	String username;
	byte[] imageArray;
	ArrayList<Long> myQuestionId;

	/**
	 * The constructor of the class
	 * 
	 * @param username
	 *            An unique user ID of the user.
	 */
	public Author(String username) {
		super();
		this.username = username;
		this.myQuestionId = new ArrayList<Long>();
	}

	/**
	 * Ask a question
	 * 
	 * @param questionId
	 *            An unique ID of the new question.
	 */
	public void addAQuestion(long questionId) {
		this.myQuestionId.add(questionId);
	}

	/**
	 * Modify the current user name
	 * 
	 * @param newUsername
	 *            The new user name.
	 */
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}

	/**
	 * Return the ID of the author's Question
	 * 
	 * @return the ID of the author's Question.
	 */
	public ArrayList<Long> getAuthorQuestionId() {
		return this.myQuestionId;
	}

	/**
	 * Return the user name of the user
	 * 
	 * @return the user name of the user.
	 */
	public String getUsername() {
		return this.username;
	}
}
