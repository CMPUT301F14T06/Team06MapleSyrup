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

	public boolean setUserName(String userName, int userID) {
		// TODO Auto-generated method stub
		if (authorExist(userName) != null) {
			return false;
		} else {
			authorList.get(userID - 1).setUserName(userName);
			return true;
		}
	}

	// if Login name exist then login otherwise create a new account
	public boolean addAuthor(String newLoginName) {
		if (authorExist(newLoginName) != null) {
			return false;
		} else {
			Author newAuthor = new Author(newLoginName, authorList.size() + 1);
			authorList.add(newAuthor);
			return true;
		}
	}

	public Integer authorExist(String newLoginName) {
		Integer existPosition = null;
		// TODO Auto-generated method stub
		for (int i = 0; i < authorList.size(); i++) {
			if (authorList.get(i).getUserName().trim()
					.equals(newLoginName.trim())) {
				existPosition = i;
				break;
			}
		}
		return existPosition;
	}
	
	public static String getUserName(int userID){
		if(userID >= 1){
			authorList.get(userID - 1).getUserName();
		}
		else{
			authorList.get(0).getUserName();
		}
		return "";
		
	}
	
	public int size() {
		return authorList.size();
	}
}
