package ca.ualbert.app.comparator;

import java.util.Comparator;

import ca.ualberta.app.models.Question;

/**
 * A Comparator which compare and sort 2 Question objects by time posted.
 */
public class DateComparator implements Comparator<Question> {
	/**
	 * A method which compares the difference between 2 Question's post time
	 * while sorting.
	 * 
	 * @param a
	 *            left hand side Comment object.
	 * @param b
	 *            right hand side Comment object.
	 * @return -1 if left hand side Question is posted later than right hand side
	 *         		Question 
	 *           1 otherwise
	 */
	
	@Override
	public int compare(Question a, Question b) {
		if (a.getID() >= b.getID()) {
			
			return -1;
			
		} else {
			
			return 1;
			
		}
	}

}
