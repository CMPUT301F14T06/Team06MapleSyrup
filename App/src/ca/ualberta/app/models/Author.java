package ca.ualberta.app.models;

import java.util.ArrayList;
import android.graphics.Bitmap;

public class Author extends User {
	// final static boolean loginState = true;

	String username;
	Bitmap image;
	public static ArrayList<Long> myQuestionId;

	public Author(String username) {
		super();
		this.username = username;

	}

	public void addAQuestion(long questionId) {
		myQuestionId.add(questionId);
	}

	public void setUsername(String newUsername) {
		this.username = newUsername;
	}

	public ArrayList<Long> getAuthorQuestionId() {
		return this.myQuestionId;
	}

	public String getUsername() {
		return this.username;
	}
}
