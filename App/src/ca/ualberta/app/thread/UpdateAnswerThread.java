package ca.ualberta.app.thread;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;

public class UpdateAnswerThread extends Thread {
	private Question question;
	private Answer answer;
	private QuestionListManager questionListManager;

	
	public UpdateAnswerThread(Question question,Answer answer) {
		this.answer = answer;
		this.question = question;
		this.questionListManager = new QuestionListManager();

	}

	@Override
	public void run() {
		question.updateAnswer(answer);
		questionListManager.updateQuestion(question);

		// Give some time to get updated info
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
