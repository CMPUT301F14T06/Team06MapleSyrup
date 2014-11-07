package ca.ualberta.app.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.User;

/**
 * 
 * @author Anni
 * 
 */
public class CacheController {
	/**
	 * load question map from local file
	 * 
	 * @param context
	 * @param FILENAME
	 * @return questionMap
	 */
	public Map<Long, Question> favoriteMap;
	public Map<Long, Question> localCacheMap;
	public ArrayList<Long> favoriteId;
	public ArrayList<Long> localCacheId;
	private String FAVMAP = "favMap.sav";
	private String LOCALMAP = "localMap.sav";
	private String FAVID = "favId.sav";
	private String LOCALID = "localId.sav";

	public CacheController(Context mcontext) {
		favoriteMap = loadMapFromFile(mcontext, FAVMAP);
		localCacheMap = loadMapFromFile(mcontext, LOCALMAP);
		favoriteId = loadIdFromFile(mcontext, FAVID);
		localCacheId = loadIdFromFile(mcontext, LOCALID);
	}

	public Map<Long, Question> getFavoriteMap() {
		return favoriteMap;
	}

	public Map<Long, Question> getLocalCacheMap() {
		return localCacheMap;

	}

	public ArrayList<Long> getFavoriteId() {
		return favoriteId;

	}

	public ArrayList<Long> getLocalCacheId() {
		return localCacheId;

	}

	public boolean hasFavorited(Question question) {
		if (favoriteMap.get(question.getID()) == null)
			return false;
		return true;
	}

	public boolean hasSaved(Question question) {
		if (localCacheMap.get(question.getID()) == null)
			return false;
		return true;
	}

	public void addFavQuestions(Context mcontext, Question question) {
		// favoriteMap = loadMapFromFile(mcontext, FAVMAP);
		favoriteMap.put(question.getID(), question);
		// favoriteId = loadIdFromFile(mcontext, FAVID);
		favoriteId.add(question.getID());
		saveInFile(mcontext, favoriteMap, FAVMAP);
		saveInFile(mcontext, favoriteId, FAVID);
	}

	public void addLocalQuestions(Context mcontext, Question question) {
		localCacheMap.put(question.getID(), question);
		localCacheId.add(question.getID());
		saveInFile(mcontext, localCacheMap, LOCALMAP);
		saveInFile(mcontext, localCacheId, LOCALID);
	}

	public void removeFavQuestions(Context mcontext, Question question) {
		// favoriteMap = loadMapFromFile(mcontext, FAVMAP);
		favoriteMap.remove(question.getID());
		// favoriteId = loadIdFromFile(mcontext, FAVID);
		for (int i = 0; i < favoriteId.size(); i++) {
			if (favoriteId.get(i) == question.getID()) {
				favoriteId.remove(i);
				break;
			}
		}
		saveInFile(mcontext, favoriteMap, FAVMAP);
		saveInFile(mcontext, favoriteId, FAVID);
	}

	public void removeLocalQuestions(Context mcontext, Question question) {
		localCacheMap.remove(question.getID());
		for (int i = 0; i < localCacheId.size(); i++) {
			if (localCacheId.get(i) == question.getID()) {
				localCacheId.remove(i);
				break;
			}
		}
		saveInFile(mcontext, localCacheMap, LOCALMAP);
		saveInFile(mcontext, localCacheId, LOCALID);
	}

	public void updateFavQuestions(Context mcontext, Question question) {
		if (favoriteMap.get(question.getID()) != null) {
			favoriteMap.remove(question.getID());
			favoriteMap.put(question.getID(), question);
			saveInFile(mcontext, favoriteMap, FAVMAP);
		}
	}

	public void clear() {
		favoriteMap.clear();
		localCacheMap.clear();
	}

	public void addAll(Context mcontext, Map<Long, Question> tempFav,
			Map<Long, Question> tempSav) {
		favoriteMap.putAll(tempFav);
		localCacheMap.putAll(tempSav);
		saveInFile(mcontext, favoriteMap, FAVMAP);
		saveInFile(mcontext, localCacheMap, LOCALMAP);

	}

	public void updateLocalQuestions(Context mcontext, Question question) {
		if (localCacheMap.get(question.getID()) != null) {
			localCacheMap.remove(question.getID());
			localCacheMap.put(question.getID(), question);
			saveInFile(mcontext, localCacheMap, LOCALMAP);
		}
	}

	public QuestionList getFavoriteQuestionList() {
		QuestionList questionList = new QuestionList();
		questionList.getCollection().addAll(this.favoriteMap.values());
		return questionList;
	}

	public QuestionList getLocalQuestionsList() {
		QuestionList questionList = new QuestionList();
		questionList.getCollection().addAll(this.localCacheMap.values());
		return questionList;
	}

	public ArrayList<Long> loadIdFromFile(Context context, String FILENAME) {
		ArrayList<Long> questionId = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<ArrayList<Long>>() {
			}.getType();
			questionId = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionId == null)
			return questionId = new ArrayList<Long>();
		return questionId;
	}

	public Map<Long, Question> loadMapFromFile(Context context, String FILENAME) {
		Map<Long, Question> questionMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<Map<Long, Question>>() {
			}.getType();
			questionMap = gson.fromJson(in, listType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (questionMap == null)
			return questionMap = new HashMap<Long, Question>();
		return questionMap;
	}

	/**
	 * save question map to local
	 * 
	 * @param context
	 * @param object
	 * @param FILENAME
	 */
	public void saveInFile(Context context, Object object, String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(object, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
