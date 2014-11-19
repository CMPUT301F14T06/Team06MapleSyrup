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

package ca.ualberta.app.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Reply;

/**
 * Combine and define functionalities of the question list.
 */
public class QuestionListController {

	private QuestionList questionList;

	/**
	 * Return the list of Questions If the list does not exist, create one.
	 * 
	 * @return questionList The list of questions.
	 */
	public QuestionList getQuestionList() {
		if (questionList == null) {
			questionList = new QuestionList();
		}
		return questionList;
	}

	/**
	 * Return the array list of questions in the question list
	 * 
	 * @return the array list of questions in the question list.
	 */
	public ArrayList<Question> getQuestionArrayList() {
		return getQuestionList().getArrayList();
	}

	/**
	 * Add a new question in the list of questions
	 * 
	 * @param newQuestion
	 *            The new question that needs to be saved into the question
	 *            list.
	 */
	public void addQuestion(Question newQuestion) {
		getQuestionList().addQuestion(newQuestion);
	}

	/**
	 * Remove a question form the list of questions
	 * 
	 * @param position
	 *            the position of the question in the list.
	 */
	public void removeQuestion(int position) {
		getQuestionList().removeQuestion(position);
	}

	/**
	 * Return the total number of the questions
	 * 
	 * @return the size of the question list.
	 */
	public int size() {
		return getQuestionList().size();
	}

	/**
	 * Return content of a question
	 * 
	 * @param position
	 *            the position of the question.
	 * @return the content of the question.
	 */
	public Question getQuestion(int position) {
		return getQuestionList().getQuestion(position);
	}

	/**
	 * Add a reply to a question
	 * 
	 * @param newReply
	 *            A new reply.
	 * @param position
	 *            the position of the question.
	 */
	public void addReplyToQ(Reply newReply, int position) {
		getQuestionList().addReplyToQ(newReply, position);
	}

	/**
	 * Add a reply to an answer
	 * 
	 * @param newReply
	 *            A new reply.
	 * @param q_position
	 *            the position of the question in the question list.
	 * @param a_position
	 *            the position of the answer in the question detail.
	 */
	public void addReplyToA(Reply newReply, int q_position, int a_position) {
		getQuestionList().addReplyToA(newReply, q_position, a_position);
	}

	/**
	 * Add an answer to the question
	 * 
	 * @param newAnswer
	 *            A new answer.
	 * @param position
	 *            The position of the question.
	 */
	public void addAnswerToQ(Answer newAnswer, int position) {
		getQuestionList().addAnswerToQ(newAnswer, position);
	}

	/**
	 * Return all answers in answer list of a question
	 * 
	 * @param position
	 *            the position of the question.
	 * @return All answers in answer list of a question.
	 */
	public ArrayList<Answer> getAnswers(int position) {
		return getQuestionList().getAnswers(position);
	}

	/**
	 * Return all replies in reply list of a question
	 * 
	 * @param position
	 *            The position of the question.
	 * @return all replies in reply list of aquestion.
	 */
	public ArrayList<Reply> getReplys(int position) {
		return getQuestionList().getReplys(position);
	}

	/**
	 * Return all replies in reply list(s) of an answer
	 * 
	 * @param q_position
	 *            the position of the question.
	 * @param a_position
	 *            the position of the answer.
	 * 
	 * @return all replies in reply list(s) of an answer.
	 */
	public ArrayList<Reply> getReplysOfAnswer(int q_position, int a_position) {
		return getQuestionList().getReplysOfAnswer(q_position, a_position);
	}

	/**
	 * Return the list of answer of a question
	 * 
	 * @param position
	 *            The position of the question.
	 * @return the list of answer of a question.
	 */
	public List<Answer> getAnswerList(int position) {
		return getQuestionList().getAnswerList(position);
	}

	/**
	 * Return the list of reply of a question/ an answer
	 * 
	 * @param position
	 *            The position of the question/ the answer.
	 * @return the list of reply of a question/ an answer.
	 */
	public List<Reply> getReplyList(int position) {
		return getQuestionList().getReplyList(position);
	}

	/**
	 * Clear the question list
	 */
	public void clear() {
		getQuestionList().getList().clear();
	}

	/**
	 * Load question of a searching result to the question list
	 * 
	 * @param searchQuestions
	 *            a question list contains a searching result.
	 */
	public void addAll(QuestionList searchQuestions) {
		getQuestionList().getList().addAll(searchQuestions.getList());
	}

	/**
	 * Return the position of the given question
	 * 
	 * @param question
	 *            The given question.
	 * 
	 * @return the position of the given question.
	 */
	public int getQuestionPosition(Question question) {
		return getQuestionList().getArrayList().indexOf(question);
	}

	/**
	 * Return the position of the given answer
	 * 
	 * @param answer
	 *            The given answer.
	 * @param position_q
	 *            the position of the corresponding question.
	 * 
	 * @return the position of the given answer.
	 */
	public int getAnswerPosition(Answer answer, int position_q) {
		return getAnswers(position_q).indexOf(answer);
	}

	/**
	 * Return the position of the given reply of a question
	 * 
	 * @param position_q
	 *            The position of the corresponding question.
	 * @param reply
	 *            The given reply.
	 * 
	 * @return the position of the given reply of a question.
	 */
	public int getReplyPosition(int position_q, Reply reply) {
		return getReplys(position_q).indexOf(reply);
	}

	/**
	 * Return the position of the given reply of an answer to a question
	 * 
	 * @param position_q
	 *            The position of the corresponding question.
	 * @param position_a
	 *            The position of the corresponding answer.
	 * @param reply
	 *            THe given reply.
	 * 
	 * @return the position of the given reply of an answer to a question.
	 */
	public int getReplyPositionOfAnswer(int position_q, int position_a,
			Reply reply) {
		return getReplysOfAnswer(position_q, position_a).indexOf(reply);
	}

	/**
	 * Load the question list from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the question list.
	 */
	public static QuestionList loadFromFile(Context context, String FILENAME) {
		QuestionList question = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<QuestionList>() {
			}.getType();
			question = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (question == null)
			return question = new QuestionList();
		return question;
	}

	/**
	 * save question list to local file
	 * 
	 * @param context
	 *            The context.
	 * @param question
	 *            The questionList.
	 * @param FILENAME
	 *            The name of the file.
	 */
	public static void saveInFile(Context context, QuestionList questionList,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(questionList, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
