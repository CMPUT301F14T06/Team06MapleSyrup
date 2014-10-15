package ca.ualberta.app.models;

public class Answer extends InputsModel {
	ReplyList replyList;

	public Answer(String content, String authorLoginName) {
		super(content, authorLoginName);
		replyList = new ReplyList();
	}

	public void addReply(Reply newReply) {
		replyList.addReply(newReply);
	}

}
