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
	Map<Long, String> idNameMap;

	/**
	 * the constructor of the class
	 */
	public AuthorMap() {
		authorMap = new HashMap<String, Author>();
		idNameMap = new HashMap<Long, String>();
	}

	/**
	 * Return the author map
	 * 
	 * @return authorMap A map that maps author Id and the Author object.
	 */
	public Map<String, Author> getAuthorMap() {
		return authorMap;
	}

	/**
	 * Return the author map
	 * 
	 * @return authorMap A map that maps user ID and the Author's name.
	 */
	public Map<Long, String> getIdNameMap() {
		return idNameMap;
	}

	public Boolean hasAuthor(String username) {
		if (authorMap.containsKey(username))
			return true;
		return false;
	}

	public Author getAuthor(String username) {
		return authorMap.get(username);
	}

	public Author getAuthor(Long userId) {
		return authorMap.get(idNameMap.get(userId));
	}

	public String getUsername(Long userId) {
		return idNameMap.get(userId);
	}

	/**
	 * If Login name exist then login otherwise create a new account
	 * 
	 * @param newAuthor
	 *            the new author.
	 */
	public void addAuthor(Author newAuthor) {
		String newUsername = newAuthor.getUsername();
		Long newUserId = newAuthor.getUserId();
		idNameMap.put(newUserId, newUsername);
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
		idNameMap.clear();
		authorMap.clear();
	}

	/**
	 * Show all authors in the searching result
	 * 
	 * @param searchAuthors
	 *            the searching result.
	 */
	public void putAll(AuthorMap searchAuthors) {
		idNameMap.putAll(searchAuthors.getIdNameMap());
		authorMap.putAll(searchAuthors.getAuthorMap());
	}

	public void removeAuthor(Long userId) {
		String username = authorMap.get(userId).getUsername();
		idNameMap.remove(userId);
		authorMap.remove(username);
	}

}
