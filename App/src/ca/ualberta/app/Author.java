package ca.ualberta.app;

public class Author extends User{
	final static boolean loginState = true;
	String userName;
	int userID;
	String email;
	QuestionList favorite;
	QuestionList questionList;
	
	public String getUserName(){
		return this.userName;
	}
	
	public int getUserID(){
		return this.userID;
	}
	
	public String getEmail(){
		return this.email;
	}

	public void setEmail(String userEmail) {
		this.email = userEmail;
	}

	public void setName(String Name) {
		this.userName = Name;
	}

	public void setID(int ID) {
		this.userID = ID;
	}
}
