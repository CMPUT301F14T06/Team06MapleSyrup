package ca.ualberta.app;

import java.util.ArrayList;
import java.util.List;

public class ReplyList {
	ArrayList<Reply> replyList;
	
	public ReplyList() {
		replyList = new ArrayList<Reply>();
	}
	
	// return a List
	public List<Reply> getList() {
		return replyList;
	}

	// return an ArrayList
	public ArrayList<Reply> getArrayList() {

		return replyList;
	}

	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	public void removeReply(int position) {
		replyList.remove(position);
	}

	public int size() {
		return replyList.size();
	}

	// get the item at current position
	public Reply get(int position) {
		return replyList.get(position);
	}
	
}
