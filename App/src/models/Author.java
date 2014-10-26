package models;

public class Author extends User {
	// final static boolean loginState = true;

	String userName;
	String email;

	public Author(String userName) {
		super();
		this.userName = userName;

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

	public String getEmail() {
		return this.email;
	}

}
