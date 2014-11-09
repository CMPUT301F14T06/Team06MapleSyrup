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

import android.graphics.Bitmap;

public class Question extends InputsModel {

	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;
	Boolean selected = false;
	long upvoteCount_question;
	long answerCount;
	long ID_question;
	long total_score = 0;
	long TheHighestAnswerUpvote = 0;

	public Question(String content, String userName, String title, Bitmap image) {
		super(content, userName, title, image);
		replyList = new ArrayList<Reply>();
		answerList = new ArrayList<Answer>();
		this.ID_question = new Date().getTime();
		answerCount = 0;
		upvoteCount_question = 0;
	}

	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	public void addAnswer(Answer newAnswer) {
		this.answerList.add(newAnswer);
		this.answerCount = answerList.size();
		calcCurrentTotalScore();
	}

	public boolean ifSelected() {
		return this.selected;
	}

	public void select() {
		this.selected = true;
	}

	public void unSelect() {
		this.selected = false;
	}

	public String getTitle() {
		return this.title;
	}

	public long getID() {
		return this.ID_question;
	}

	public void setID(long newID){
		this.ID_question = newID;
	}
	
	public int getAnswerCount() {
		return this.answerList.size();
	}

	public ArrayList<Answer> getAnswers() {
		return this.answerList;
	}

	public ArrayList<Reply> getReplys() {
		return this.replyList;
	}

	public int getAnswerPosition(Answer answer) {
		return this.answerList.indexOf(answer);
	}

	public int getReplyPosition(Reply reply) {
		return this.replyList.indexOf(reply);
	}

	public long getQuestionUpvoteCount() {
		return upvoteCount_question;
	}

	public void upvoteQuestion() {
		upvoteCount_question++;
	}

	public void setUpvoteCount(long newUpvoteCount){
		this.upvoteCount_question = newUpvoteCount;
	}
	
	public void calcCurrentTotalScore() {
		total_score = 0;
		for (Answer answer : this.answerList) {
			total_score += answer.getAnswerUpvoteCount();
		}
		total_score += upvoteCount_question;
	}

	public long getTotalScore() {
		return total_score;
	}

	public long getTheHighestAnswerUpvote() {
		for (Answer answer : this.answerList) {		
			if (answer.getAnswerUpvoteCount() >= TheHighestAnswerUpvote){
				TheHighestAnswerUpvote = answer.getAnswerUpvoteCount();
			}
		}
		return TheHighestAnswerUpvote;
	}
	
	public void updateAnswer(Answer answer){
		int index = this.answerList.indexOf(answer);
		this.answerList.remove(index);
		this.answerList.add(answer);
	}
}
