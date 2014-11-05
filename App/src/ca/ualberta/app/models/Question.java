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
}
