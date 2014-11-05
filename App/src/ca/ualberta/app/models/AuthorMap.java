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
	public void addAuthor(Author newAuthor) {
		String newUsername = newAuthor.getUsername();
		authorMap.put(newUsername, newAuthor);
	}

	public int size() {
		return authorMap.size();
	}

	public void clear() {
		authorMap.clear();
	}

	public void putAll(AuthorMap searchAuthors) {
		authorMap.putAll(searchAuthors.getMap());

	}

	public void put(String username, Author author) {
		// TODO Auto-generated method stub
		authorMap.put(username, author);
	}

}
