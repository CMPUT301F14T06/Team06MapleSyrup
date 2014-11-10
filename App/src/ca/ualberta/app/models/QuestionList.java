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
import java.util.Collection;
import java.util.List;

/**
 * This class defines most of the functionalities that a question should have.
 */
public class QuestionList {
	// Question List
	ArrayList<Question> questionList;

	// Map<Long, Question> questionMap;

	/**
	 * The constructor of the class
	 */
	public QuestionList() {
		// questionMap = new HashMap<Long, Question>();
		questionList = new ArrayList<Question>();
	}

	/**
	 * Return the question list
	 * 
	 * @return The question list.
	 */
	public List<Question> getList() {
		return questionList;
	}

	/**
	 * Return the arraylist of questions
	 * 
	 * @return The arraylist of questions.
	 */
	public ArrayList<Question> getArrayList() {

		return questionList;
	}

	/**
	 * Return the collection of questions
	 * 
	 * @return The collection of questions.
	 */
	public Collection<Question> getCollection() {

		return questionList;
	}

	// public Map<Long, Question> getMap() {
	// return questionMap;
	// }

	/**
	 * Add a question to the question list
	 * 
	 * @param newQuestion
	 *            the question needs to be added to the question list.
	 */
	public void addQuestion(Question newQuestion) {
		this.questionList.add(newQuestion);
		// this.questionMap.put(newQuestion.getID(), newQuestion);
	}

	/**
	 * Remove a question from the question list
	 * 
	 * @param position
	 *            The position of the question.
	 */
	public void removeQuestion(int position) {
		questionList.remove(position);
		// questionMap.remove(questionList.get(position).getID());
	}

	/**
	 * Return the total number of questions in the list
	 * 
	 * @return The size of the question list.
	 */
	public int size() {
		return questionList.size();
	}

	/**
	 * Return the question with a given position
	 * 
	 * @param position
	 *            The given position.
	 * 
	 * @return The question.
	 */
	public Question getQuestion(int position) {
		return questionList.get(position);
	}

	/**
	 * Add a reply to the given question
	 * 
	 * @param newReply
	 *            The reply.
	 * @param position
	 *            The position of the given question.
	 */
	public void addReplyToQ(Reply newReply, int position) {
		getReplys(position).add(newReply);
	}

	/**
	 * Add a reply to a given answer to a given question
	 * 
	 * @param newReply
	 *            The reply.
	 * @param q_position
	 *            The position of the given question.
	 * @param a_position
	 *            The position of the given answer.
	 */
	public void addReplyToA(Reply newReply, int q_position, int a_position) {
		getAnswers(q_position).get(a_position).addReply(newReply);
	}

	/**
	 * Add an answer to a given question
	 * 
	 * @param newAnswer
	 *            the answer.
	 * @param position
	 *            The given question.
	 */
	public void addAnswerToQ(Answer newAnswer, int position) {
		getAnswers(position).add(newAnswer);
	}

	/**
	 * Return all answers to a question
	 * 
	 * @param position
	 *            the position of the given question.
	 * 
	 * @return the list contains all answers to the given question.
	 */
	public ArrayList<Answer> getAnswers(int position) {
		return questionList.get(position).answerList;
	}

	/**
	 * Return all replies of a given question
	 * 
	 * @param position
	 *            the position of the given question.
	 * 
	 * @return the list contains all replies of the given question.
	 */
	public ArrayList<Reply> getReplys(int position) {
		return questionList.get(position).replyList;
	}

	/**
	 * Return all the replies of the given answer to a given question
	 * 
	 * @param q_position
	 *            The position of the given question.
	 * @param a_position
	 *            The position of the given answer.
	 * 
	 * @return the list contains all the replies of the given answer to a given
	 *         question.
	 */
	public ArrayList<Reply> getReplysOfAnswer(int q_position, int a_position) {
		return questionList.get(q_position).answerList.get(a_position).replyList;
	}

	/**
	 * Return the answer at the given position in the answer list
	 * 
	 * @param position
	 *            The given position of the answer.
	 * 
	 * @return The answer at the given position.
	 */
	public List<Answer> getAnswerList(int position) {
		return questionList.get(position).answerList;
	}

	/**
	 * Return replies at the given position in the question list.
	 * 
	 * @param position
	 *            the given position of the replies
	 * 
	 * @return replies at the given position in the question list.
	 */
	public List<Reply> getReplyList(int position) {
		return questionList.get(position).replyList;
	}

	/**
	 * This method has not been completed yet
	 * 
	 * @return null.
	 */
	public QuestionList sortByPicture() {
		return null;
	}

	/**
	 * This method has not been completed yet
	 * 
	 * @return null.
	 */
	public QuestionList sortByScore() {
		return null;
	}

}