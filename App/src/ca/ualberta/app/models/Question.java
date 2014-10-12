package ca.ualberta.app.models;

public class Question extends AuthorInputs {

	AnswerList answerList;
	String title;
	long answerCount;

	public Question(String content, String author, String title) {
		super(content, author);
		this.title = title;
		answerList = new AnswerList();
		answerCount = 0;
	}

	public void addAnswer(Answer newAnswer) {
		answerList.addAnswer(newAnswer);
		answerCount = answerList.size();
	}
}
