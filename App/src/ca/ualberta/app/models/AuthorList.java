package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.List;

public class AuthorList {
	static ArrayList<Author> authorList;

	public AuthorList() {
		authorList = new ArrayList<Author>();
	}

	// return a List
	public List<Author> getList() {
		return authorList;
	}

	// return an ArrayList
	public ArrayList<Author> getArrayList() {
		return authorList;
	}

	public boolean setUserName(String userName, String newUserName) {
		if (authorExistPosition(newUserName) != null) {
			return false;
		} else {
			authorList.get(authorExistPosition(userName)).setUserName(newUserName);
			return true;
		}
	}

	// if Login name exist then login otherwise create a new account
	public boolean addAuthor(String newUserName) {
		if (authorExistPosition(newUserName) != null) {
			return false;
		} else {
			Author newAuthor = new Author(newUserName);
			authorList.add(newAuthor);
			return true;
		}
	}

	public Integer authorExistPosition(String newUserName) {
		Integer existPosition = null;
		for (int i = 0; i < authorList.size(); i++) {
			if (authorList.get(i).getUserName().trim()
					.equals(newUserName.trim())) {
				existPosition = i;
				break;
			}
		}
		return existPosition;
	}
	
	
	public int size() {
		return authorList.size();
	}
}
