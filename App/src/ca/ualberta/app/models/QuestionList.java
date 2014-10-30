package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.List;

public class QuestionList {
	// Question List
	ArrayList<Question> questionList;

	public QuestionList() {
		questionList = new ArrayList<Question>();
	}

	// return a List
	public List<Question> getList() {
		return questionList;
	}

	// return an ArrayList
	public ArrayList<Question> getArrayList() {

		return questionList;
	}

	public void addQuestion(Question newQuestion) {
		this.questionList.add(newQuestion);
	}

	public void removeQuestion(int position) {
		questionList.remove(position);
	}

	public int size() {
		return questionList.size();
	}

	// get the item at current position
	public Question getQuestion(int position) {
		return questionList.get(position);
	}

	public void addReplyToQ(Reply newReply, int position) {
		getReplys(position).add(newReply);
	}

	public void addReplyToA(Reply newReply, int q_position, int a_position) {
		getAnswers(q_position).get(a_position).addReply(newReply);
	}

	public void addAnswerToQ(Answer newAnswer, int position) {
		getAnswers(position).add(newAnswer);
	}

	public ArrayList<Answer> getAnswers(int position) {
		return questionList.get(position).answerList;
	}

	public ArrayList<Reply> getReplys(int position) {
		return questionList.get(position).replyList;
	}

	public ArrayList<Reply> getReplysOfAnswer(int q_position, int a_position) {
		return questionList.get(q_position).answerList.get(a_position).replyList;
	}
	
	public List<Answer> getAnswerList(int position) {
		return questionList.get(position).answerList;
	}

	public List<Reply> getReplyList(int position) {
		return questionList.get(position).replyList;
	}

	public QuestionList sortByPicture() {
		// TODO Auto-generated method stub
		return null;
	}

	public QuestionList sortByScore() {
		// TODO Auto-generated method stub
		return null;
	}

}