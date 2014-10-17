package ca.ualberta.app.models;


public class Author extends User {
	//final static boolean loginState = true;

	String loginName;
	String authorName;
	int authorID;
	String email;
	
	//authorName is initial as loginName, but can be change later
	//loginName is unique and unable to be change once is created
	public Author(String loginName, int authorID) {
		this.loginName = loginName;
		this.authorName = loginName;
		this.authorID = authorID;
	}

	public void setName(String authorName) {
		// TODO Auto-generated method stub
		this.authorName = authorName;
	}

	public void setEmail(String authorEmail) {
		this.email = authorEmail;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public String getAuthorName() {
		return this.authorName;
	}
	
	public int getID() {
		return this.authorID;
	}

	public String getEmail() {
		return this.email;
	}

}
