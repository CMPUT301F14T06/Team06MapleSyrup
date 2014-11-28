package ca.ualberta.app.controller;

import java.util.ArrayList;
import ca.ualberta.app.models.Reply;

public class ReplyListController {

	private ArrayList<Reply> ReplyList;

	public ArrayList<Reply> getReplyList() {
		if (ReplyList == null) {
			ReplyList = new ArrayList<Reply>();
		}
		return ReplyList;
	}

	public void addReply(Reply newReply) {
		getReplyList().add(newReply);
	}

	public void removeQuestion(Reply newReply) {
		getReplyList().remove(newReply);
	}

	public int size() {
		return getReplyList().size();
	}

	public Reply getReply(int position) {
		return getReplyList().get(position);
	}

	public void clear() {
		getReplyList().clear();
	}

	public int getReplyPosition(Reply reply) {
		return getReplyList().indexOf(reply);
	}
	
	public void addAll(ArrayList<Reply> newReplyList) {
		getReplyList().addAll(newReplyList);
	}
}
