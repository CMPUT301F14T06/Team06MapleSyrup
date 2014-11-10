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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.network.data.Hits;
import ca.ualberta.app.network.data.SearchCommand;
import ca.ualberta.app.network.data.SearchHit;
import ca.ualberta.app.network.data.SearchResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * This manager manages all operations on questions and question IDs.
 */
public class QuestionListManager {
	private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t06/question/_search";
	private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t06/question/";
	private static final String TAG = "QuestionSearch";

	private Gson gson;

	/**
	 * The constructor of the class Initial an instant for gson
	 */
	public QuestionListManager() {
		gson = new Gson();
	}

	/**
	 * Get a question with the specified id
	 * 
	 * @param ID
	 *            the specified id of a question.
	 * 
	 * @return null.
	 */
	public Question getQuestion(long ID) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(RESOURCE_URL + ID);

		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);
			SearchHit<Question> sr = parseQuestionHit(response);
			return sr.getSource();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the list of questions with the specified ID's in ID list
	 * 
	 * @param listID
	 *            The list of question ID's.
	 * 
	 * @return questionList The list of questions.
	 */
	public QuestionList getQuestionList(ArrayList<Long> listID) {
		QuestionList questionList = new QuestionList();

		for (long ID : listID) {
			questionList.addQuestion(getQuestion(ID));
		}
		return questionList;
	}

	/**
	 * Get the map of questions with the specified ID's in ID list
	 * 
	 * @param listID
	 *            The list of question ID's.
	 * 
	 * @return questionMap The map of questions.
	 */
	public Map<Long, Question> getQuestionMap(ArrayList<Long> listID) {
		Map<Long, Question> questionMap = new HashMap<Long, Question>();

		for (long ID : listID) {
			questionMap.put(ID, getQuestion(ID));
		}
		return questionMap;
	}

	/**
	 * Get questions with the specified search string. If the search does not
	 * specify fields, it searches on all the fields.
	 * 
	 * @param searchString
	 *            The keyword for searching questions.
	 * @param field
	 *            the specified searching field
	 * @param size
	 *            The size of the string buffer for searching.
	 * @param from
	 *            The starting position of searching.
	 * @param lable
	 *            The label of the string buffer.
	 * 
	 * @return the searching result.
	 */
	public QuestionList searchQuestions(String searchString, String field,
			long from, long size) {
		QuestionList result = new QuestionList();

		// TODO: Implement search questions using ElasticSearch
		if (searchString == null || "".equals(searchString)) {
			searchString = "*";
		}

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost searchRequest = createSearchRequest(searchString, field,
					from, size);
			HttpResponse response = httpClient.execute(searchRequest);

			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

			SearchResponse<Question> esResponse = parseSearchResponse(response);
			Hits<Question> hits = esResponse.getHits();

			if (hits != null) {
				if (hits.getHits() != null) {
					for (SearchHit<Question> sesr : hits.getHits()) {
						result.addQuestion(sesr.getSource());
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
	 * Add a new question to he online server
	 * 
	 * @param question
	 *            The question.
	 */
	public void addQuestion(Question question) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPost addRequest = new HttpPost(RESOURCE_URL + question.getID());

			StringEntity stringEntity = new StringEntity(gson.toJson(question));
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
	 * Update a exist question on the online server
	 * 
	 * @param question
	 *            The question.
	 */
	public void updateQuestion(Question question) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpPut updateRequest = new HttpPut(RESOURCE_URL + question.getID());

			StringEntity stringEntity = new StringEntity(gson.toJson(question));
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
	 * Deletes the question with a given question ID
	 * 
	 * @param questionId
	 *            The given question ID.
	 */
	public void deleteQuestion(long questionId) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpDelete deleteRequest = new HttpDelete(RESOURCE_URL + questionId);
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
	 * @param searchString
	 *            The keyword for searching.
	 * @param field
	 *            the specified searching field
	 * @param size
	 *            The size of the string buffer for searching.
	 * @param from
	 *            The starting position of searching.
	 * 
	 * @return searchRequest.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private HttpPost createSearchRequest(String searchString, String field,
			long from, long size) throws UnsupportedEncodingException {

		HttpPost searchRequest = new HttpPost(SEARCH_URL);

		String[] fields = null;
		if (field != null) {
			fields = new String[1];
			fields[0] = field;
		}

		SearchCommand command = new SearchCommand(searchString, from, size);

		String query = command.getJsonCommand();
		Log.i(TAG, "Json command: " + query);

		StringEntity stringEntity;
		stringEntity = new StringEntity(query);

		searchRequest.setHeader("Accept", "application/json");
		searchRequest.setEntity(stringEntity);

		return searchRequest;
	}

	/**
	 * Load a question form online server
	 * 
	 * @param response
	 *            The online server connection response.
	 * 
	 * @return null.
	 */
	private SearchHit<Question> parseQuestionHit(HttpResponse response) {

		try {
			String json = getEntityContent(response);
			Type searchHitType = new TypeToken<SearchHit<Question>>() {
			}.getType();

			SearchHit<Question> sr = gson.fromJson(json, searchHitType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Parses the response of a search
	 * 
	 * @param response
	 *            The online server connection response.
	 * 
	 * @return esResponse The parsed response of a search.
	 * 
	 * @throws IOException
	 */
	private SearchResponse<Question> parseSearchResponse(HttpResponse response)
			throws IOException {
		String json;
		json = getEntityContent(response);

		Type searchResponseType = new TypeToken<SearchResponse<Question>>() {
		}.getType();

		SearchResponse<Question> esResponse = gson.fromJson(json,
				searchResponseType);

		return esResponse;
	}

	/**
	 * Gets content from an HTTP response
	 * 
	 * @param response
	 *            The online server connection response.
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
