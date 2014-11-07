package ca.ualberta.app.thread;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;

public class UpdateAnswerThread extends Thread {
	private Question question;
	private Answer answer_gonna_update;
	private Answer answer_onServer;
	private QuestionListManager questionListManager;
	private int ansPosition;

	public UpdateAnswerThread(Question question, Answer answer_gonna_update) {
		this.answer_gonna_update = answer_gonna_update;
		this.question = question;
		this.questionListManager = new QuestionListManager();

	}

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
