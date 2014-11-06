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
	public static Map<Long, Question> loadFromFile(Context context,
			String FILENAME) {
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
	 * @param authorMap
	 * @param FILENAME
	 */
	public static void saveInFile(Context context,
			Map<Long, Question> questionMap, String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(questionMap, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addFavQuestions(Context mcontext, String FAVMAP,
			Question question) {
		User.favoriteId = CacheController.loadFromFile(mcontext, FAVMAP);
		User.favoriteId.put(question.getID(), question);
		CacheController.saveInFile(mcontext, User.favoriteId, FAVMAP);
	}

	public static void addLocalQuestions(Context mcontext, String LOCALMAP,
			Question question) {
		User.localCacheId = CacheController.loadFromFile(mcontext, LOCALMAP);
		User.localCacheId.put(question.getID(), question);
		CacheController.saveInFile(mcontext, User.localCacheId, LOCALMAP);
	}

	public static void updateFavQuestions(Context mcontext, String FAVMAP,
			Question question) {
		if (User.favoriteId.get(question.getID()) != null) {
			User.favoriteId = CacheController.loadFromFile(mcontext, FAVMAP);
			User.favoriteId.remove(question.getID());
			User.favoriteId.put(question.getID(), question);
			CacheController.saveInFile(mcontext, User.favoriteId, FAVMAP);
		}
	}

	public static void updateLocalQuestions(Context mcontext, String LOCALMAP,
			Question question) {
		if (User.localCacheId.get(question.getID()) != null) {
			User.localCacheId = CacheController
					.loadFromFile(mcontext, LOCALMAP);
			User.localCacheId.remove(question.getID());
			User.localCacheId.put(question.getID(), question);
			CacheController.saveInFile(mcontext, User.localCacheId, LOCALMAP);
		}
	}

	public static QuestionList loadFavoriteQuestion() {

		QuestionList questionList = new QuestionList();
		questionList.getCollection().addAll(User.favoriteId.values());
		return questionList;
	}

	public static QuestionList loadLocalQuestions() {
		QuestionList questionList = new QuestionList();
		questionList.getCollection().addAll(User.localCacheId.values());

		return questionList;
	}

	public static ArrayList<Long> getFavIdList() {
		QuestionList questionList = loadFavoriteQuestion();
		ArrayList<Long> favIdList = new ArrayList<Long>();
		for (Question question : questionList.getArrayList()) {
			favIdList.add(question.getID());
		}
		return favIdList;

	}

	public static ArrayList<Long> getLocalIdList() {
		QuestionList questionList = loadLocalQuestions();
		ArrayList<Long> localIdList = new ArrayList<Long>();
		for (Question question : questionList.getArrayList()) {
			localIdList.add(question.getID());
		}
		return localIdList;

	}

}
