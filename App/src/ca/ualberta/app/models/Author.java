package ca.ualberta.app.models;


public class Author extends User {
	//final static boolean loginState = true;

	String userName;
	int userID;
	String email;
	
	
	public Author(String userName, int userID) {
		super();
		this.userName = userName;
		this.userID = userID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String authorEmail) {
		this.email = authorEmail;
	}

	public String getUserName() {
		return this.userName;
	}
	
	public int getID() {
		return this.userID;
	}

	public String getEmail() {
		return this.email;
	}

}
