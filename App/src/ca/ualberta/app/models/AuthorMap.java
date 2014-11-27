/*
 * Copyright 2014 Anni Dai
 * Copyright 2014 Bicheng Yan
 * Copyright 2014 Liwen Chen
 * Copyright 2014 Liang Jingjing
 * Copyright 2014 Xiaocong Zhou
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.app.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains some operations on author map
 */
public class AuthorMap {
	Map<String, Author> authorMap;

	/**
	 * the constructor of the class
	 */
	public AuthorMap() {
		authorMap = new HashMap<String, Author>();
	}

	/**
	 * Return the author map
	 * 
	 * @return authorMap A map that maps user name and the Author object.
	 */
	public Map<String, Author> getMap() {
		return authorMap;
	}

	public Boolean hasAuthor(String username) {
		if (authorMap.containsKey(username))
			return true;
		return false;
	}

	/**
	 * If Login name exist then login otherwise create a new account
	 * 
	 * @param newAuthor
	 *            the new author.
	 */
	public void addAuthor(Author newAuthor) {
		String newUsername = newAuthor.getUsername();
		authorMap.put(newUsername, newAuthor);
	}

	/**
	 * Return the total number of authors
	 * 
	 * @return the total number of authors.
	 */
	public int size() {
		return authorMap.size();
	}

	/**
	 * clear the author map
	 */
	public void clear() {
		authorMap.clear();
	}

	/**
	 * Show all authors in the searching result
	 * 
	 * @param searchAuthors
	 *            the searching result.
	 */
	public void putAll(AuthorMap searchAuthors) {
		authorMap.putAll(searchAuthors.getMap());

	}

	/**
	 * Map an user name to an author
	 * 
	 * @param username
	 *            The user name.
	 * @param author
	 *            The author.
	 */
	public void put(String username, Author author) {
		authorMap.put(username, author);
	}

	public void removeAuthor(String username) {
		// TODO Auto-generated method stub
		authorMap.remove(username);
	}

}
