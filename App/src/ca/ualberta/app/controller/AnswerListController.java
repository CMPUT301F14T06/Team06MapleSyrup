package ca.ualberta.app.controller;

import java.util.ArrayList;
import ca.ualberta.app.models.Answer;

/**
 * Combine and define functionalities of the answer list.
 */
public class AnswerListController {

	private ArrayList<Answer> AnswerList;

	/**
	 * Return the list of answers If the list does not exist, create one.
	 * 
	 * @return return the list of answers.
	 */
	public ArrayList<Answer> getAnswerList() {
		if (AnswerList == null) {
			AnswerList = new ArrayList<Answer>();
		}
		return AnswerList;
	}

	/**
	 * Add a new answer in the list of answer
	 * 
	 * @param newAnswer
	 *            The new answer that needs to be saved into the answer list.
	 */
	public void addAnswer(Answer newAnswer) {
		getAnswerList().add(newAnswer);
	}

	/**
	 * Remove a answer form the list of answers
	 * 
	 * @param newAnswer
	 *            the answer that need to remove.
	 */
	public void removeAnswer(Answer newAnswer) {
		getAnswerList().remove(newAnswer);
	}

	/**
	 * Return the total number of the answers
	 * 
	 * @return the size of the answer list.
	 */
	public int size() {
		return getAnswerList().size();
	}

	/**
	 * Return content of a answer
	 * 
	 * @param position
	 *            the position of the answer.
	 * @return the content of the answer.
	 */
	public Answer getAnswer(int position) {
		return getAnswerList().get(position);
	}

	/**
	 * Clear the answer list
	 */
	public void clear() {
		getAnswerList().clear();
	}

	/**
	 * Return the position of the given answer
	 * 
	 * @param answer
	 *            The given answer.
	 * 
	 * @return the position of the given answer.
	 */
	public int getAnswerPosition(Answer answer) {
		return getAnswerList().indexOf(answer);
	}

	/**
	 * add an answer list to answerList
	 * 
	 * @param newAnswerList
	 *            the new answerList
	 */
	public void addAll(ArrayList<Answer> newAnswerList) {
		getAnswerList().addAll(newAnswerList);
	}
}
