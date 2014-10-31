package ca.ualberta.app.models;

import java.util.HashMap;
import java.util.Map;

public class AuthorMap {
	Map<String, Author> authorMap;

	public AuthorMap() {
		authorMap = new HashMap<String, Author>();
	}

	// return Map<String, Author>
	public Map<String, Author> getMap() {
		return authorMap;
	}

	public boolean setUsername(String username, String newUsername) {
		if (authorMap.get(newUsername) != null) {
			return false;
		} else {
			authorMap.get(username).setUsername(newUsername);
			return true;
		}
	}

	// if Login name exist then login otherwise create a new account
	public boolean addAuthor(String newUsername) {
		if (authorMap.get(newUsername) != null) {
			return false;
		} else {
			Author newAuthor = new Author(newUsername);
			authorMap.put(newUsername, newAuthor);
			return true;
		}
	}

	public int size() {
		return authorMap.size();
	}

}
