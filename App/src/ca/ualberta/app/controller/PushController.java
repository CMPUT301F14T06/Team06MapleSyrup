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
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;

/**
 * used to save un-posted question, answer and reply
 */
public class PushController {
	public Map<Long, Question> waitingListMap_Question;
	public ArrayList<Long> waitingListId_Question;
	public Map<Long, Answer> waitingListMap_Answer;
	public ArrayList<Long> waitingListId_Answer;
	public Map<Long, Reply> waitingListMap_Reply;
	public ArrayList<Long> waitingListId_Reply;
	public Map<Long, Author> waitingList_Author;
	private String WAITMAP_Q = "waitMap_Question.sav";
	private String WAITID_Q = "waitId_Question.sav";
	private String WAITMAP_A = "waitMap_Answer.sav";
	private String WAITID_A = "waitId_Answer.sav";
	private String WAITMAP_R = "waitMap_Reply.sav";
	private String WAITID_R = "waitId_Reply.sav";
	private String WAIT_AUTHOR = "wait_Author.sav";
	private String questionTitle;

	/**
	 * The constructor of the class load maps from local file
	 * 
	 * @param mcontext
	 *            the context.
	 */
	public PushController(Context mcontext) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		waitingListId_Answer = loadIdFromFile(mcontext, WAITID_A);
		waitingListMap_Reply = loadMapFromFile_R(mcontext, WAITMAP_R);
		waitingListId_Reply = loadIdFromFile(mcontext, WAITID_R);
		waitingList_Author = loadMapFromFile_Author(mcontext, WAIT_AUTHOR);
	}

	/**
	 * Return the question map of the waitingList question from local file
	 * 
	 * @param mcontext
	 *            the context.
	 * 
	 * @return the question map of the waitingList question from the local file.
	 */
	public Map<Long, Question> getWaitingListMap(Context mcontext) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		return waitingListMap_Question;

	}

	/**
	 * Return the answer ID of the waitingList answer from local file
	 * 
	 * @param mcontext
	 *            the context.
	 * 
	 * @return the answer ID of the waitingList answer from the local file.
	 */
	public ArrayList<Long> getWaitingAnswerListId(Context mcontext) {
		waitingListId_Answer = loadIdFromFile(mcontext, WAITID_A);
		return waitingListId_Answer;

	}

	/**
	 * Return the reply ID of the waitingList reply from local file
	 * 
	 * @param mcontext
	 *            the context.
	 * 
	 * @return the reply ID of the waitingList reply from the local file.
	 */
	public ArrayList<Long> getWaitingReplyListId(Context mcontext) {
		waitingListId_Reply = loadIdFromFile(mcontext, WAITID_R);
		return waitingListId_Reply;

	}

	/**
	 * Return the question ID of the waitingList question from local file
	 * 
	 * @param mcontext
	 *            the context.
	 * 
	 * @return the question ID of the waitingList question from the local file.
	 */
	public ArrayList<Long> getWaitingListId(Context mcontext) {
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		return waitingListId_Question;

	}

	/**
	 * Return the author list of the waitingList from local file
	 * 
	 * @param mcontext
	 *            the context
	 * @return the author that need to be updated/add while Internet connected
	 */
	public Map<Long, Author> getWaitingAuthorMap(Context mcontext) {
		waitingList_Author = loadMapFromFile_Author(mcontext, WAIT_AUTHOR);
		return waitingList_Author;
	}

	/**
	 * Return whether the local file for local question contain the question.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 * 
	 * @return true if the local file for favorite question has the question,
	 *         false if not.
	 */
	public boolean hasWaited(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		if (waitingListMap_Question.get(question.getID()) == null)
			return false;
		return true;
	}

	/**
	 * Return whether the local file for waiting list contain the answer.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param answer
	 *            the answer.
	 * 
	 * @return true if the local file for waiting list question has the answer,
	 *         false if not.
	 */
	public boolean hasWaited_A(Context mcontext, Answer answer) {
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		if (waitingListMap_Answer.get(answer.getID()) == null)
			return false;
		return true;
	}

	/**
	 * Return whether the local file for waiting list contain the reply.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param reply
	 *            the reply.
	 * 
	 * @return true if the local file for waiting list has the reply, false if
	 *         not.
	 */
	public boolean hasWaited_R(Context mcontext, Reply reply) {
		waitingListMap_Reply = loadMapFromFile_R(mcontext, WAITMAP_R);
		if (waitingListMap_Reply.get(reply.getID()) == null)
			return false;
		return true;
	}

	/**
	 * Return whether the local file for author map
	 * 
	 * @param mcontext
	 *            the context.
	 * @param userId
	 *            the user Id.
	 * 
	 * @return true if the local file has the author, false if not.
	 */
	public boolean hasWaited_Author(Context mcontext, Long userId) {
		waitingList_Author = loadMapFromFile_Author(mcontext, WAIT_AUTHOR);
		if (waitingList_Author.get(userId) == null)
			return false;
		return true;
	}

	/**
	 * Save a question into the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void addWaitngListQuestions(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		waitingList_Author = loadMapFromFile_Author(mcontext, WAIT_AUTHOR);
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		if (!hasWaited(mcontext, question)) {
			waitingListMap_Question.put(question.getID(), question);
			waitingListId_Question.add(question.getID());
			if (!hasWaited_Author(mcontext, User.author.getUserId()))
				waitingList_Author.put(User.author.getUserId(), User.author);
			else {
				waitingList_Author.remove(User.author.getUserId());
				waitingList_Author.put(User.author.getUserId(), User.author);
			}
			saveInFile(mcontext, waitingList_Author, WAIT_AUTHOR);
			saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
			saveInFile(mcontext, waitingListId_Question, WAITID_Q);
		}
	}

	/**
	 * Save a author into the the local file.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param author
	 *            the author.
	 */
	public void addWaitngListAuthors(Context mcontext, Author author) {
		waitingList_Author = loadMapFromFile_Author(mcontext, WAIT_AUTHOR);
		waitingList_Author.put(author.getUserId(), author);
		saveInFile(mcontext, waitingList_Author, WAIT_AUTHOR);
	}

	/**
	 * Save a answer into the the local file of the WaitingList answer.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param answer
	 *            the answer.
	 */
	public void addWaitngListAnswers(Context mcontext, Answer answer,
			String questionTitle) {
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		waitingListId_Answer = loadIdFromFile(mcontext, WAITID_A);
		if (!hasWaited_A(mcontext, answer)) {
			waitingListMap_Answer.put(answer.getID(), answer);
			waitingListId_Answer.add(answer.getID());
			saveInFile(mcontext, waitingListMap_Answer, WAITMAP_A);
			saveInFile(mcontext, waitingListId_Answer, WAITID_A);
		}
	}

	/**
	 * Save a reply into the the local file of the WaitingList reply.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param reply
	 *            the reply.
	 */
	public void addWaitngListReplies(Context mcontext, Reply reply,
			String questionTitle) {
		waitingListMap_Reply = loadMapFromFile_R(mcontext, WAITMAP_R);
		waitingListId_Reply = loadIdFromFile(mcontext, WAITID_R);
		if (!hasWaited_R(mcontext, reply)) {
			waitingListMap_Reply.put(reply.getID(), reply);
			waitingListId_Reply.add(reply.getID());
			saveInFile(mcontext, waitingListMap_Reply, WAITMAP_R);
			saveInFile(mcontext, waitingListId_Reply, WAITID_R);
		}
	}

	/**
	 * Remove a question into the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void removeWaitingListQuestion(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		waitingListId_Question = loadIdFromFile(mcontext, WAITID_Q);
		waitingListMap_Question.remove(question.getID());
		for (int i = 0; i < waitingListId_Question.size(); i++) {
			if (waitingListId_Question.get(i) == question.getID()) {
				waitingListId_Question.remove(i);
				break;
			}
		}
		saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
		saveInFile(mcontext, waitingListId_Question, WAITID_Q);
	}

	/**
	 * Remove a question into the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 */
	public void removeWaitingListQuestionList(Context mcontext) {
		waitingListMap_Question.clear();
		waitingListId_Question.clear();
		saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
		saveInFile(mcontext, waitingListId_Question, WAITID_Q);
	}

	/**
	 * Remove a answer into the the local file of the WaitingList answer.
	 * 
	 * @param mcontext
	 *            the context.
	 */
	public void removeWaitingListAnswerList(Context mcontext) {
		waitingListMap_Answer.clear();
		waitingListId_Answer.clear();
		saveInFile(mcontext, waitingListMap_Answer, WAITMAP_A);
		saveInFile(mcontext, waitingListId_Answer, WAITID_A);
	}

	/**
	 * Remove a reply into the the local file of the WaitingList reply.
	 * 
	 * @param mcontext
	 *            the context.
	 */
	public void removeWaitingListReplyList(Context mcontext) {
		waitingListMap_Reply.clear();
		waitingListId_Reply.clear();
		saveInFile(mcontext, waitingListMap_Reply, WAITMAP_R);
		saveInFile(mcontext, waitingListId_Reply, WAITID_R);
	}

	/**
	 * Remove a author into the the local file.
	 * 
	 * @param mcontext
	 *            the context.
	 */
	public void removeWaitingListAuthorMap(Context mcontext) {
		waitingList_Author.clear();
		saveInFile(mcontext, waitingList_Author, WAIT_AUTHOR);
	}

	/**
	 * Update a question in the the local file of the WaitingList questions.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param question
	 *            the question.
	 */
	public void updateWaitingListQuestion(Context mcontext, Question question) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		if (waitingListMap_Question.get(question.getID()) != null) {
			waitingListMap_Question.remove(question.getID());
			waitingListMap_Question.put(question.getID(), question);
			saveInFile(mcontext, waitingListMap_Question, WAITMAP_Q);
		}
	}

	/**
	 * Update a answer in the the local file of the WaitingList answers.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param answer
	 *            the answer.
	 */
	public void updateWaitingListAnswer(Context mcontext, Answer answer) {
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		if (waitingListMap_Answer.get(answer.getID()) != null) {
			waitingListMap_Answer.remove(answer.getID());
			waitingListMap_Answer.put(answer.getID(), answer);
			saveInFile(mcontext, waitingListMap_Answer, WAITMAP_A);
		}
	}

	/**
	 * Update a reply in the the local file of the WaitingList reply.
	 * 
	 * @param mcontext
	 *            the context.
	 * @param reply
	 *            the reply.
	 */
	public void updateWaitingListReply(Context mcontext, Reply reply) {
		waitingListMap_Reply = loadMapFromFile_R(mcontext, WAITMAP_R);
		if (waitingListMap_Reply.get(reply.getID()) != null) {
			waitingListMap_Reply.remove(reply.getID());
			waitingListMap_Reply.put(reply.getID(), reply);
			saveInFile(mcontext, waitingListMap_Reply, WAITMAP_R);
		}
	}

	/**
	 * Load and return the WaitingList question list
	 * 
	 * @return the local question list.
	 */
	public QuestionList getWaitingQuestionList(Context mcontext) {
		waitingListMap_Question = loadMapFromFile(mcontext, WAITMAP_Q);
		QuestionList questionList = new QuestionList();
		questionList.getCollection().addAll(
				this.waitingListMap_Question.values());
		return questionList;
	}

	/**
	 * Load and return the WaitingList answer list
	 * 
	 * @return the local answer list.
	 */
	public ArrayList<Answer> getWaitingAnswerList(Context mcontext) {
		waitingListMap_Answer = loadMapFromFile_A(mcontext, WAITMAP_A);
		ArrayList<Answer> answerList = new ArrayList<Answer>();
		answerList.addAll(this.waitingListMap_Answer.values());
		return answerList;
	}

	/**
	 * Load and return the WaitingList reply list
	 * 
	 * @return the local reply list.
	 */
	public ArrayList<Reply> getWaitingReplyList(Context mcontext) {
		waitingListMap_Reply = loadMapFromFile_R(mcontext, WAITMAP_R);
		ArrayList<Reply> replyList = new ArrayList<Reply>();
		replyList.addAll(this.waitingListMap_Reply.values());
		return replyList;
	}

	/**
	 * Load and return the WaitingList author list
	 * 
	 * @return the local author list.
	 */
	public ArrayList<Author> getWaitingAuthorList(Context mcontext) {
		waitingList_Author = loadMapFromFile_Author(mcontext, WAIT_AUTHOR);
		ArrayList<Author> authorList = new ArrayList<Author>();
		authorList.addAll(this.waitingList_Author.values());
		return authorList;
	}

	/**
	 * Load the ID's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the ID.
	 */
	public ArrayList<Long> loadIdFromFile(Context context, String FILENAME) {
		ArrayList<Long> questionId = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<ArrayList<Long>>() {
			}.getType();
			questionId = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionId == null)
			return questionId = new ArrayList<Long>();
		return questionId;
	}

	/**
	 * Load the question Map's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the Map of the question(s).
	 */
	public Map<Long, Question> loadMapFromFile(Context context, String FILENAME) {
		Map<Long, Question> questionMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Question>>() {
			}.getType();
			questionMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionMap == null)
			return questionMap = new HashMap<Long, Question>();
		return questionMap;
	}

	/**
	 * Load the answer Map's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the Map of the answer(s).
	 */
	public Map<Long, Answer> loadMapFromFile_A(Context context, String FILENAME) {
		Map<Long, Answer> answerMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Answer>>() {
			}.getType();
			answerMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (answerMap == null)
			return answerMap = new HashMap<Long, Answer>();
		return answerMap;
	}

	/**
	 * Load the reply Map's from the file with given name.
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of the local file.
	 * 
	 * @return the Map of the reply(s).
	 */
	public Map<Long, Reply> loadMapFromFile_R(Context context, String FILENAME) {
		Map<Long, Reply> replyMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Reply>>() {
			}.getType();
			replyMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (replyMap == null)
			return replyMap = new HashMap<Long, Reply>();
		return replyMap;
	}

	/**
	 * Load the author from the file
	 * 
	 * @param context
	 *            The context.
	 * @param FILENAME
	 *            The name of from the file
	 * 
	 * @return the Map of the reply(s).
	 */
	public Map<Long, Author> loadMapFromFile_Author(Context context,
			String FILENAME) {
		Map<Long, Author> authorMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Author>>() {
			}.getType();
			authorMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (authorMap == null)
			return authorMap = new HashMap<Long, Author>();
		return authorMap;
	}

	/**
	 * save a map to local file
	 * 
	 * @param context
	 *            The context.
	 * @param object
	 *            The map.
	 * @param FILENAME
	 *            The name of the file.
	 */
	public void saveInFile(Context context, Object object, String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(object, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set the question Title
	 * 
	 * @param questionTitle
	 *            the question title
	 */
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	/**
	 * get the question title
	 * 
	 * @return the question title
	 */
	public String getQuestionTitle() {
		return this.questionTitle;
	}
}
