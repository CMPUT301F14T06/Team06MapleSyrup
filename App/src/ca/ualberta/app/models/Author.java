package ca.ualberta.app.models;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Author extends User {
	// final static boolean loginState = true;

	String username;
	String email;
	Bitmap image;
	ArrayList<Long> authorQuestionId;

	public Author(String username) {
		super();
		this.username = username;

	}

	public void setUsername(String newUsername) {
		this.username = newUsername;
	}

	public void setEmail(String authorEmail) {
		this.email = authorEmail;
	}

	public String getUsername() {
		return this.username;
	}

	public String getEmail() {
		return this.email;
	}

	//
	// public void setImage(String newUsername) {

	// }
}
