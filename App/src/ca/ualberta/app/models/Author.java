package ca.ualberta.app.models;

/**
 * @author  bicheng
 */
public class Author extends User {
	//final static boolean loginState = true;
	/**
	 * @uml.property  name="loginName"
	 */
	String loginName;
	/**
	 * @uml.property  name="authorName"
	 */
	String authorName;
	int authorID;
	/**
	 * @uml.property  name="email"
	 */
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

	/**
	 * @param authorEmail
	 * @uml.property  name="email"
	 */
	public void setEmail(String authorEmail) {
		this.email = authorEmail;
	}

	/**
	 * @return
	 * @uml.property  name="loginName"
	 */
	public String getLoginName() {
		return this.loginName;
	}

	/**
	 * @return
	 * @uml.property  name="authorName"
	 */
	public String getAuthorName() {
		return this.authorName;
	}

	public int getID() {
		return this.authorID;
	}

	/**
	 * @return
	 * @uml.property  name="email"
	 */
	public String getEmail() {
		return this.email;
	}

}
