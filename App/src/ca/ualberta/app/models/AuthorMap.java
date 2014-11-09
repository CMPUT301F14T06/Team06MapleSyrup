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
		authorMap.put(username, author);
	}

}
