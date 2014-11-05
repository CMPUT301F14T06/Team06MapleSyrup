package ca.ualberta.app.thread;

import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListManager;

public class UpdateQuestionThread extends Thread {
	private Question question;
	private QuestionListManager questionListManager;
	
	public UpdateQuestionThread(Question question) {
		this.question = question;
		this.questionListManager = new QuestionListManager();
	}

	@Override
	public void run() {
		questionListManager.updateQuestion(question);

		// Give some time to get updated info
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
