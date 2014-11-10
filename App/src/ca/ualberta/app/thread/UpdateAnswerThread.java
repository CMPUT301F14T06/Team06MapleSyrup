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
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;

/**
 * This is the functionality of the thread which is used to update a answer in online server.
 * 
 * @author Bicheng
 *
 */
public class UpdateAnswerThread extends Thread {
	private Question question;
	private Answer answer_gonna_update;
	private Answer answer_onServer;
	private QuestionListManager questionListManager;
	private int ansPosition;

	/**
	 * The constructor of the class
	 * 
	 * @param question The corresponding question.
	 * @param answer_gonna_update The new answer.
	 */
	public UpdateAnswerThread(Question question, Answer answer_gonna_update) {
		this.answer_gonna_update = answer_gonna_update;
		this.question = question;
		this.questionListManager = new QuestionListManager();

	}

	/**
	 * start the thread and update the new answer
	 */
	@Override
	public void run() {
		ansPosition = question.getAnswerPosition(answer_gonna_update);
		answer_onServer = questionListManager.getQuestion(question.getID())
				.getAnswers().get(ansPosition);
		
		if (answer_onServer.getAnswerUpvoteCount() >= answer_gonna_update.getAnswerUpvoteCount()){
			answer_gonna_update.setUpvoteCount(answer_onServer.getAnswerUpvoteCount());
			answer_gonna_update.upvoteAnswer();
		}

		question.updateAnswer(answer_gonna_update);
		questionListManager.updateQuestion(question);

		// Give some time to get updated info
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
