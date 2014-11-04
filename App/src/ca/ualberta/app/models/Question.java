package ca.ualberta.app.models;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Question extends InputsModel {
	ArrayList<Reply> replyList;
	ArrayList<Answer> answerList;
	Boolean selected=false;
	public Question(String content, String userName, String title,
			Bitmap image) {
		super(content, userName, title, image);
		replyList = new ArrayList<Reply>();
		answerList = new ArrayList<Answer>();
	}


	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	public void addAnswer(Answer newAnswer) {
		this.answerList.add(newAnswer);
		this.answerCount = answerList.size();
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
	
	public long getID(){
		return this.ID_question;
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
	
	public int getReplyPosition(Reply reply){
		return this.replyList.indexOf(reply);
	}

}
