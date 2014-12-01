package ca.ualberta.app.controller;

import java.util.ArrayList;
import ca.ualberta.app.models.Reply;

/**
 * Combine and define functionalities of the reply list.
 */
public class ReplyListController {

	private ArrayList<Reply> ReplyList;

	/**
	 * Return the list of replies If the list does not exist, create one.
	 * 
	 * @return return the list of replies.
	 */
	public ArrayList<Reply> getReplyList() {
		if (ReplyList == null) {
			ReplyList = new ArrayList<Reply>();
		}
		return ReplyList;
	}

	/**
	 * Add a new reply in the list of reply
	 * 
	 * @param newReply
	 *            The new reply that needs to be saved into the reply list.
	 */
	public void addReply(Reply newReply) {
		getReplyList().add(newReply);
	}

	/**
	 * Remove a reply form the list of replies
	 * 
	 * @param newReply
	 *            the reply
	 */
	public void removeReply(Reply newReply) {
		getReplyList().remove(newReply);
	}

	/**
	 * Return the total number of the replies
	 * 
	 * @return the size of the reply list.
	 */
	public int size() {
		return getReplyList().size();
	}

	/**
	 * Return content of a reply
	 * 
	 * @param position
	 *            the position of the reply.
	 * @return the content of the reply.
	 */
	public Reply getReply(int position) {
		return getReplyList().get(position);
	}

	/**
	 * Clear the reply list
	 */
	public void clear() {
		getReplyList().clear();
	}

	/**
	 * Return the position of the given reply
	 * 
	 * @param reply
	 *            The given reply.
	 * 
	 * @return the position of the given reply.
	 */
	public int getReplyPosition(Reply reply) {
		return getReplyList().indexOf(reply);
	}

	/**
	 * add an reply list to replyList
	 * 
	 * @param newReplyList
	 *            the new ReplyList
	 */
	public void addAll(ArrayList<Reply> newReplyList) {
		getReplyList().addAll(newReplyList);
	}
}
