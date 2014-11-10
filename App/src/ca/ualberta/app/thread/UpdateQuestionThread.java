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

package ca.ualberta.app.thread;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Question;

/**
 * This is the functionality of the thread which is used to update 
 * question details in online server.
 * 
 * @author Bicheng
 *
 */
public class UpdateQuestionThread extends Thread {
	private Question question_gonna_update;
	private Question question_onServer;
	private QuestionListManager questionListManager;

	/**
	 * The constructor of the class
	 * 
	 * @param question_update the question instant with new detials.
	 */
	public UpdateQuestionThread(Question question_update) {
		this.question_gonna_update = question_update;
		this.questionListManager = new QuestionListManager();
	}

	/**
	 * start the thread for updating the question instant with new detials
	 */
	@Override
	public void run() {
		//get the current upvote that stored on server
		question_onServer = questionListManager.getQuestion(question_gonna_update
				.getID());
		
		//get the newest upvote and upvote it 
		if (question_onServer.getQuestionUpvoteCount() >= question_gonna_update
				.getQuestionUpvoteCount()) {
			question_gonna_update.setUpvoteCount(question_onServer.getQuestionUpvoteCount());
			question_gonna_update.upvoteQuestion();
		} 
		
		//update the newest upvote to server
		questionListManager.updateQuestion(question_gonna_update);


		// Give some time to get updated info
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
