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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains all functionalities an answer object should have.
 */
public class Answer extends InputsModel {
	ArrayList<Reply> replyList;
	long upvoteCount_answer;
	long ID_answer;
	Map<String, Author> upvotedPerson;

	/**
	 * The constructor of the class
	 * 
	 * @param content
	 *            The content of the answer.
	 * @param userName
	 *            the user name of the user who answers the answer.
	 * @param image
	 *            The image inside the answer.
	 */
	public Answer(String content, String userName, String imageString) {
		super(content, userName, imageString);
		this.replyList = new ArrayList<Reply>();
		this.ID_answer = new Date().getTime() - 100;
		this.upvoteCount_answer = 0;
		upvotedPerson = new HashMap<String, Author>();
	}

	/**
	 * Add a reply to the answer
	 * 
	 * @param newReply
	 *            the new reply to the answer.
	 */
	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	/**
	 * Return the reply list of the answer
	 * 
	 * @return replyList The reply list of the answer.
	 */
	public List<Reply> getReplyList() {
		return this.replyList;
	}

	/**
	 * Return the reply arraylist of the answer
	 * 
	 * @return replyList The reply arraylist of the answer.
	 */
	public ArrayList<Reply> getReplyArrayList() {
		return this.replyList;
	}

	/**
	 * Return the position of a reply in the reply list
	 * 
	 * @param reply
	 *            The given reply.
	 * 
	 * @return The position of given reply.
	 */
	public int getReplyPosition(Reply reply) {
		return this.replyList.indexOf(reply);
	}

	/**
	 * increase the counter counts the up vote number
	 */
	public boolean upvoteAnswer() {
		String username = User.author.getUsername();
		if (upvotedPerson.get(username) == null) {
			upvotedPerson.put(username, User.author);
			upvoteCount_answer++;
			return true;
		}
		return false;
	}

	/**
	 * Update the upvote counter
	 * 
	 * @param newUpvoteCount
	 *            the new value of the counter.
	 */
	public void setUpvoteCount(long newUpvoteCount) {
		this.upvoteCount_answer = newUpvoteCount;
	}

	/**
	 * Return the upvote number of the question
	 * 
	 * @return the upvote number of the question.
	 */
	public long getAnswerUpvoteCount() {
		return this.upvoteCount_answer;
	}
}
