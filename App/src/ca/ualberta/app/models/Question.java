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
import java.util.Map;

/**
 * This class defines all functionalities a Question should have.
 */
public class Question extends InputsModel {

	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;
	Map<String, Author> upvotedPerson;
	Boolean selected = false;
	long upvoteCount_question;
	long answerCount;
	long ID_question;
	long total_score = 0;
	long TheHighestAnswerUpvote = 0;

	/**
	 * The constructor of the class
	 * 
	 * @param content
	 *            The content.
	 * @param userName
	 *            The ID of the user who posted the question.
	 * @param title
	 *            The title of the question.
	 * @param image
	 *            The image belongs to the question.
	 */
	public Question(String content, String userName, String title,
			String imageString) {
		super(content, userName, title, imageString);
		replyList = new ArrayList<Reply>();
		answerList = new ArrayList<Answer>();
		upvotedPerson = new HashMap<String, Author>();
		this.ID_question = new Date().getTime();
		answerCount = 0;
		upvoteCount_question = 0;
	}

	/**
	 * Add a new reply to the question
	 * 
	 * @param newReply
	 *            The new reply.
	 */
	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	/**
	 * Add a new answer to the question
	 * 
	 * @param newAnswer
	 *            The new question.
	 */
	public void addAnswer(Answer newAnswer) {
		this.answerList.add(newAnswer);
		this.answerCount = answerList.size();
		calcCurrentTotalScore();
	}

	/**
	 * Check if the current question is selected
	 * 
	 * @return true if the question is selected, else return false otherwise.
	 */
	public boolean ifSelected() {
		return this.selected;
	}

	/**
	 * select the question
	 */
	public void select() {
		this.selected = true;
	}

	/**
	 * un-select a question
	 */
	public void unSelect() {
		this.selected = false;
	}

	/**
	 * Return the title of the question
	 * 
	 * @return title The title of the question.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Return the ID of the question
	 * 
	 * @return ID_question The ID of the question.
	 */
	public long getID() {
		return this.ID_question;
	}

	/**
	 * Set the ID of a question
	 * 
	 * @param newID
	 *            The new ID.
	 */
	public void setID(long newID) {
		this.ID_question = newID;
	}

	/**
	 * Return the total number of answers of the question
	 * 
	 * @return the size of the answer list of the question.
	 */
	public int getAnswerCount() {
		return this.answerList.size();
	}

	/**
	 * Return the answer list of the question
	 * 
	 * @return The answer list of the question.
	 */
	public ArrayList<Answer> getAnswers() {
		return this.answerList;
	}

	/**
	 * Return the reply list of the question
	 * 
	 * @return The reply list of the question
	 */
	public ArrayList<Reply> getReplys() {
		return this.replyList;
	}

	/**
	 * Return the position of an given answer of a question
	 * 
	 * @param answer
	 *            The given answer.
	 * 
	 * @return The position of an given answer.
	 */
	public int getAnswerPosition(Answer answer) {
		return this.answerList.indexOf(answer);
	}

	/**
	 * Return the position of an given reply question
	 * 
	 * @param reply
	 *            The given reply.
	 * 
	 * @return The position of an given reply.
	 */
	public int getReplyPosition(Reply reply) {
		return this.replyList.indexOf(reply);
	}

	/**
	 * Return the total count of upvote of a question
	 * 
	 * @return upvoteCount_question The total count of upvote of a question.
	 */
	public long getQuestionUpvoteCount() {
		return upvoteCount_question;
	}

	/**
	 * Increase the upvote counter
	 */
	public boolean upvoteQuestion() {
		String username = User.author.getUsername();
		if (upvotedPerson.get(username) == null) {
			upvotedPerson.put(username, User.author);
			upvoteCount_question++;
			return true;
		}
		return false;
	}

	/**
	 * Set the number of the upvote counter
	 * 
	 * @param newUpvoteCount
	 *            The new value of the upvote counter.
	 */
	public void setUpvoteCount(long newUpvoteCount) {
		this.upvoteCount_question = newUpvoteCount;
	}

	/**
	 * Calculate the current total score of a question/answer
	 */
	public void calcCurrentTotalScore() {
		total_score = 0;
		for (Answer answer : this.answerList) {
			total_score += answer.getAnswerUpvoteCount();
		}
		total_score += upvoteCount_question;
	}

	/**
	 * Return the current total score of a question/answer
	 * 
	 * @return The current total score of a question/answer.
	 */
	public long getTotalScore() {
		return total_score;
	}

	/**
	 * Return the answer with highest upvote number
	 * 
	 * @return The answer with highest upvote number.
	 */
	public long getTheHighestAnswerUpvote() {
		for (Answer answer : this.answerList) {
			if (answer.getAnswerUpvoteCount() >= TheHighestAnswerUpvote) {
				TheHighestAnswerUpvote = answer.getAnswerUpvoteCount();
			}
		}
		return TheHighestAnswerUpvote;
	}

	/**
	 * update an answer to the question
	 * 
	 * @param answer
	 *            The new answer.
	 */
	public void updateAnswer(Answer answer) {
		int index = this.answerList.indexOf(answer);
		this.answerList.remove(index);
		this.answerList.add(answer);
	}
}
