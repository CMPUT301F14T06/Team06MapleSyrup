package ca.ualberta.app.thread;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Question;

public class UpdateQuestionThread extends Thread {
	private Question question_gonna_update;
	private Question question_onServer;
	private QuestionListManager questionListManager;

	public UpdateQuestionThread(Question question_update) {
		this.question_gonna_update = question_update;
		this.questionListManager = new QuestionListManager();
	}

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
