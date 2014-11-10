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

public class QuestionListController {

	private QuestionList questionList;

	public QuestionList getQuestionList() {
		if (questionList == null) {
			questionList = new QuestionList();
		}
		return questionList;
	}

	public ArrayList<Question> getQuestionArrayList() {
		return getQuestionList().getArrayList();
	}

	public void addQuestion(Question newQuestion) {
		getQuestionList().addQuestion(newQuestion);
	}

	public void removeQuestion(int position) {
		getQuestionList().removeQuestion(position);
	}

	public int size() {
		return getQuestionList().size();
	}

	public Question getQuestion(int position) {
		return getQuestionList().getQuestion(position);
	}

	public void addReplyToQ(Reply newReply, int position) {
		getQuestionList().addReplyToQ(newReply, position);
	}

	public void addReplyToA(Reply newReply, int q_position, int a_position) {
		getQuestionList().addReplyToA(newReply, q_position, a_position);
	}

	public void addAnswerToQ(Answer newAnswer, int position) {
		getQuestionList().addAnswerToQ(newAnswer, position);
	}

	public ArrayList<Answer> getAnswers(int position) {
		return getQuestionList().getAnswers(position);
	}

	public ArrayList<Reply> getReplys(int position) {
		return getQuestionList().getReplys(position);
	}

	public ArrayList<Reply> getReplysOfAnswer(int q_position, int a_position) {
		return getQuestionList().getReplysOfAnswer(q_position, a_position);
	}

	public List<Answer> getAnswerList(int position) {
		return getQuestionList().getAnswerList(position);
	}

	public List<Reply> getReplyList(int position) {
		return getQuestionList().getReplyList(position);
	}

	public void clear() {
		getQuestionList().getList().clear();
	}

	public void addAll(QuestionList searchQuestions) {
		getQuestionList().getList().addAll(searchQuestions.getList());
	}
	
	public int getQuestionPosition(Question question) {
		return getQuestionList().getArrayList().indexOf(question);
	}

	public int getAnswerPosition(Answer answer, int position_q) {
		return getAnswers(position_q).indexOf(answer);
	}

	public int getReplyPosition(int position_q, Reply reply) {
		return getReplys(position_q).indexOf(reply);
	}

	public int getReplyPositionOfAnswer(int position_q, int position_a,
			Reply reply) {
		return getReplysOfAnswer(position_q, position_a).indexOf(reply);
	}

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

	public static void saveInFile(Context context, QuestionList question,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(question, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
