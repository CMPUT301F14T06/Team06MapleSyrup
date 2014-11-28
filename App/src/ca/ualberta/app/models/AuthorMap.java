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
	Map<Long, Author> authorIdMap;
	Map<String, Long> authorNameMap;

	/**
	 * the constructor of the class
	 */
	public AuthorMap() {
		authorIdMap = new HashMap<Long, Author>();
		authorNameMap = new HashMap<String, Long>();
	}

	/**
	 * Return the author map
	 * 
	 * @return authorMap A map that maps author Id and the Author object.
	 */
	public Map<Long, Author> getIdMap() {
		return authorIdMap;
	}

	/**
	 * Return the author map
	 * 
	 * @return authorMap A map that maps user name and the Author's ID.
	 */
	public Map<String, Long> getNameMap() {
		return authorNameMap;
	}

	public Boolean hasAuthor(String username) {
		if (authorNameMap.containsKey(username))
			return true;
		return false;
	}

	public Author getAuthor(String username) {
		return authorIdMap.get(authorNameMap.get(username));
	}

	public Author getAuthor(Long userId) {
		return authorIdMap.get(userId);
	}

	public String getUsername(Long userId) {
		return authorIdMap.get(userId).getUsername();
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
		authorNameMap.put(newUsername, newUserId);
		authorIdMap.put(newUserId, newAuthor);
	}

	/**
	 * Return the total number of authors
	 * 
	 * @return the total number of authors.
	 */
	public int size() {
		return authorNameMap.size();
	}

	/**
	 * clear the author map
	 */
	public void clear() {
		authorNameMap.clear();
		authorIdMap.clear();
	}

	/**
	 * Show all authors in the searching result
	 * 
	 * @param searchAuthors
	 *            the searching result.
	 */
	public void putAll(AuthorMap searchAuthors) {
		authorNameMap.putAll(searchAuthors.getNameMap());
		authorIdMap.putAll(searchAuthors.getIdMap());
	}

	public void removeAuthor(Long userId) {
		String username = authorIdMap.get(userId).getUsername();
		authorNameMap.remove(username);
		authorIdMap.remove(userId);
	}

}
