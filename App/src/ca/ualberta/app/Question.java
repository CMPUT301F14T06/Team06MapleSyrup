package ca.ualberta.app;

public class Question {
	AnswerList answerList;
	ReplyList replyList;
	String question;
	long upvoteCount;
	long downvoteCount;
	long answerCount;
	public Question(String question) {
		this.question = question;
		answerList = new AnswerList();
		replyList = new ReplyList();
		upvoteCount = 0;
		downvoteCount = 0;
		answerCount = 0;
	}
	public void addAnswer(Answer newAnswer){
		answerCount = answerList.size();
		answerList.addAnswer(newAnswer);
	}
	
	public void addReply(Reply newReply){
		replyList.addReply(newReply);
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void upvote(){
		upvoteCount++;
	}
	
	public void downvote(){
		downvoteCount++;
	}
}
