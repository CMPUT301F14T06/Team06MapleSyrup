package ca.ualberta.app.models;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.app.network.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QuestionListManager {

	private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t06/question/_search";
	private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f14t06/question/";
	private static final String TAG = "QuestionSearch";

	private Gson gson;

	public QuestionListManager() {
		gson = new Gson();
	}

	/**
	 * Get a Question with the specified id
	 */
	public Question getQuestion(long id) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(RESOURCE_URL + id);

		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);
			SearchHit<Question> sr = parseMovieHit(response);
			return sr.getSource();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get Questions with the specified search string.
	 */
	public QuestionList searchQuestion(String searchString) {
		QuestionList result = new QuestionList();

		// TODO: Implement search movies using ElasticSearch
		if (searchString == null || "".equals(searchString)) {
			searchString = "*";
		}

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost searchRequest = createSearchRequest(searchString);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Adds a new Question
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
	 * Deletes the Question with the specified id
	 */
	public void deleteMovie(int questionID) {
		HttpClient httpClient = new DefaultHttpClient();

		try {
			HttpDelete deleteRequest = new HttpDelete(RESOURCE_URL + questionID);
			deleteRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(deleteRequest);
			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a search request from a search string
	 */
	private HttpPost createSearchRequest(String searchString)
			throws UnsupportedEncodingException {

		HttpPost searchRequest = new HttpPost(SEARCH_URL);

		SearchCommand command = new SearchCommand(searchString);

		String query = command.getJsonCommand();
		Log.i(TAG, "Json command: " + query);

		StringEntity stringEntity;
		stringEntity = new StringEntity(query);

		searchRequest.setHeader("Accept", "application/json");
		searchRequest.setEntity(stringEntity);

		return searchRequest;
	}

	private SearchHit<Question> parseMovieHit(HttpResponse response) {

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
