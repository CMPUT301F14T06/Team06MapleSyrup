package ca.ualberta.app.models;

public class Question extends InputsModel {

	AnswerList answerList;
	String title;
	long answerCount;
	ReplyList replyList;

	public Question(String content, String authorLoginName, String title) {
		super(content, authorLoginName);
		this.title = title;
		replyList = new ReplyList();
		answerList = new AnswerList();
		answerCount = 0;
	}

	public void addReply(Reply newReply) {
		replyList.addReply(newReply);
	}

	public void addAnswer(Answer newAnswer) {
		answerList.addAnswer(newAnswer);
		answerCount = answerList.size();
	}
}
