package ca.ualberta.app.comparator;

import java.util.Comparator;

import ca.ualberta.app.models.Question;

/**
 * A comparator class which compute 2 Comment object's score and sort them by
 * Question Upvote.
 */

public class AnswerUpvoteComparator implements Comparator<Question> {
	/**
	 * @param a
	 *            : left hand side Question
	 * @param b
	 *            : right hand side Question
	 * @return -1 if left hand side Answer's Upvote in Question a is higher than
	 *         the right hand side Question's Answer Upvote 1 otherwise.
	 */
	@Override
	public int compare(Question a, Question b) {
		if (a.getTheHighestAnswerUpvote() > b.getTheHighestAnswerUpvote()) {
			return -1;
		} else if (a.getTheHighestAnswerUpvote() < b
				.getTheHighestAnswerUpvote()) {
			return 1;
		} else if (a.getID() >= b.getID()) {
			return -1;
		} else {
			return 1;
		}
	}
}
