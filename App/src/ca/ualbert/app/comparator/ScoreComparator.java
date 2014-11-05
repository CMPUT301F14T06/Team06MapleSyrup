package ca.ualbert.app.comparator;

import java.util.Comparator;
import ca.ualberta.app.models.Question;

/**
 * A comparator class which compute 2 Comment object's score and sort them by
 * score.
 */

public class ScoreComparator implements Comparator<Question> {
	/**
	 * @param a
	 *            : left hand side Question
	 * @param b
	 *            : right hand side Question
	 * @return -1 if left hand side Question's score is higher than the right
	 *         		hand side Question's score 
	 *           1 otherwise.
	 */
	@Override
	public int compare(Question a, Question b) {
		if (a.getTotalScore() >= b.getTotalScore()) {
			return -1;
		} else {
			return 1;
		}
	}
}
