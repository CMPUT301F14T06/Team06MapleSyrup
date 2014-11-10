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

package ca.ualberta.app.ESmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.network.data.Hits;
import ca.ualberta.app.network.data.SearchCommand;
import ca.ualberta.app.network.data.SearchHit;
import ca.ualberta.app.network.data.SearchResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This manager manages all operations on authors and authors' ID's. 
 * 
 * @author Bicheng
 *
 */
public class AuthorMapManager {
	private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t06/author/_search";
	private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t06/author/";
	private static final String TAG = "AuthorLogin";

	private Gson gson;

	/**
	 * The constructor of the class
	 * Initial an instant for gson
	 */
	public AuthorMapManager() {
		gson = new Gson();
	}

	/**
	 * Get a author with the specified ID
	 * 
	 * @param username The user's specified ID.
	 * 
	 * @return null.
	 */
	public Author getAuthor(String username) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(RESOURCE_URL + username);

		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);
			SearchHit<Author> sr = parseAuthorHit(response);
			return sr.getSource();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Get authors with the specified search string. If the search does not
	 * specify fields, it searches on all the fields.
	 * 
	 * @param searchString The keyword for searching an author.
	 * @param field the specified searching field
	 * @param size The size of the string buffer for searching.
	 * @param from The starting position of searching.
	 * @param lable The lable of the string buffer.
	 * 
	 * @return the searching result.
	 */
	public AuthorMap searchAuthors(String searchString, String field, long from, long size, String lable) {
		AuthorMap result = new AuthorMap();

		// TODO: Implement search authors using ElasticSearch
		if (searchString == null || "".equals(searchString)) {
			searchString = "*";
		}

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost searchRequest = createSearchRequest(searchString, field, from, size, lable);
			HttpResponse response = httpClient.execute(searchRequest);

			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

			SearchResponse<Author> esResponse = parseSearchResponse(response);
			Hits<Author> hits = esResponse.getHits();

			if (hits != null) {
				if (hits.getHits() != null) {
					for (SearchHit<Author> sesr : hits.getHits()) {
						result.addAuthor(sesr.getSource());
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Adds a new author
	 * 
	 * @param author The new author.
	 */
	public void addAuthor(Author author) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost addRequest = new HttpPost(RESOURCE_URL
					+ author.getUsername());

			StringEntity stringEntity = new StringEntity(gson.toJson(author));
			addRequest.setEntity(stringEntity);
			addRequest.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(addRequest);
			String status = response.getStatusLine().toString();

			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update an exist author
	 * 
	 * @param author The author.
	 */
	public void updateAuthor(Author author) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPut updateRequest = new HttpPut(RESOURCE_URL
					+ author.getUsername());

			StringEntity stringEntity = new StringEntity(gson.toJson(author));
			updateRequest.setEntity(stringEntity);
			updateRequest.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(updateRequest);
			String status = response.getStatusLine().toString();

			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the Author with the specified userName
	 * 
	 * @param userName the given userName.
	 */
	public void deleteAuthor(String userName) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpDelete deleteRequest = new HttpDelete(RESOURCE_URL + userName);
			deleteRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(deleteRequest);
			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a search request from a search string and a field
	 * 
	 * @param size The size of the string buffer for searching.
	 * @param from The starting position of searching.
	 * @param lable The lable of the string buffer.
	 * 
	 * @return searchRequest The searching result.
	 */
	private HttpPost createSearchRequest(String searchString, String field, long from, long size, String lable)
			throws UnsupportedEncodingException {

		HttpPost searchRequest = new HttpPost(SEARCH_URL);

		String[] fields = null;
		if (field != null) {
			fields = new String[1];
			fields[0] = field;
		}

		SearchCommand command = new SearchCommand(searchString, from, size, lable);

		String query = command.getJsonCommand();
		Log.i(TAG, "Json command: " + query);

		StringEntity stringEntity;
		stringEntity = new StringEntity(query);

		searchRequest.setHeader("Accept", "application/json");
		searchRequest.setEntity(stringEntity);

		return searchRequest;
	}

	/**
	 * Load an author form online server
	 * 
	 * @param response The online server connection response.
	 * 
	 * @return null.
	 */
	private SearchHit<Author> parseAuthorHit(HttpResponse response) {

		try {
			String json = getEntityContent(response);
			Type searchHitType = new TypeToken<SearchHit<Author>>() {
			}.getType();

			SearchHit<Author> sr = gson.fromJson(json, searchHitType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Parses the response of a search
	 * 
	 * @param response The online server connection response.
	 * 
	 * @return esResponse The parsed response of a search.
	 * 
	 * @throws IOException
	 */
	private SearchResponse<Author> parseSearchResponse(HttpResponse response)
			throws IOException {
		String json;
		json = getEntityContent(response);

		Type searchResponseType = new TypeToken<SearchResponse<Author>>() {
		}.getType();

		SearchResponse<Author> esResponse = gson.fromJson(json,
				searchResponseType);

		return esResponse;
	}

	/**
	 * Gets content from an HTTP response
	 * 
	 * @param response The online server connection response.
	 * 
	 * @return the content from an HTTP response.
	 * 
	 * @throws IOException
	 */
	public String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}
}
