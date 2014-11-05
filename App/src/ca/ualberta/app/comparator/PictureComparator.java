package ca.ualberta.app.comparator;

import java.util.Comparator;
import ca.ualberta.app.models.Question;

/**
 * A comparator class which compares 2 Question objects and sort them by
 * picture.
 */
public class PictureComparator implements Comparator<Question> {
	/**
	 * @param a
	 *            : left hand side Comment
	 * @param b
	 *            : right hand side Comment
	 * @return -1 if right hand side Comment's picture is null but left hand
	 *         side Comment's picture is not null, 1 if right hand side
	 *         Comment's picture is not null but left hand side Comment's
	 *         picture is null, 0 otherwise.
	 */
	@Override
	public int compare(Question a, Question b) {
		if ((a.hasImage() == false) && (b.hasImage() == false)) {
			return -1;
		} 
		else if ((a.hasImage() == false) && (b.hasImage() == true)) {
			return 1;	
		} 
		else {
			return 0;
		}
	}

}