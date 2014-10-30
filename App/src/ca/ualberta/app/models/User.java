package ca.ualberta.app.models;

public class User {
	// final static boolean loginState = false;
	public QuestionList favorite;
	public User(){
		favorite = new QuestionList();
	}
}
