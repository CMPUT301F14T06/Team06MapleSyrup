package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.List;

public class AuthorList {
	ArrayList<Author> authorList;

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

	// if Login name exist then login otherwise create a new account
	public boolean addAuthor(String newLoginName) {
		if (authorExist(newLoginName) == null) {
			return false;
		} else {
			Author newAuthor = new Author(newLoginName, authorList.size() + 1);
			this.authorList.add(newAuthor);
			return true;
		}
	}

	public String getAuthorNameByLoginName(String loginName) {
		String authorName;
		int existPosition;
		existPosition = authorExist(loginName);
		authorName = authorList.get(existPosition).getAuthorName();
		return authorName;
	}

	public Integer authorExist(String newLoginName) {
		Integer existPosition = null;
		// TODO Auto-generated method stub
		for (int i = 0; i < authorList.size(); i++) {
			if (authorList.get(i).getLoginName().trim()
					.equals(newLoginName.trim())) {
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
