package ca.ualberta.app.models;

/**
 * @author  bicheng
 */
public abstract class User {
	// final static boolean loginState = false;
	/**
	 * @uml.property  name="favorite"
	 * @uml.associationEnd  
	 */
	QuestionList favorite;
}
