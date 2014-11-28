package ca.ualberta.app.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import android.content.Context;
import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateAuthorThread;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AuthorMapController {
	private AuthorMap authorMap;
	private String FILENAME = "AUTHORMAP.sav";
	private AuthorMapManager authorMapManager;
	private Context context;
	private long from = 0;
	private long size = 1000;
	private String lable = "author";

	public AuthorMapController(Context context) {
		this.context = context;
		authorMapManager = new AuthorMapManager();
		authorMap = loadFromFile(context, FILENAME);
	}

	public void clear() {
		authorMap.clear();
	}

	public void putAll(AuthorMap searchAuthors) {
		authorMap.putAll(searchAuthors);
		saveInFile(context, authorMap, FILENAME);

	}

	public AuthorMap getAuthorMap(Context context) {
		authorMap = loadFromFile(context, FILENAME);
		return loadFromFile(context, FILENAME);
	}

	public boolean hasAuthor(Context context, String username) {
		authorMap = loadFromFile(context, FILENAME);
		return authorMap.hasAuthor(username);
	}

	public void addAuthor(Context context, Author newAuthor) {
		Thread addThread = new AddThread(newAuthor);
		addThread.start();
		Thread searchThread = new SearchThread("");
		searchThread.start();
		saveInFile(context, authorMap, FILENAME);
	}

	public Author getAuthor(String username) {
		return authorMap.getAuthor(username);
	}

	public Author getAuthor(Long userId) {
		return authorMap.getAuthor(userId);
	}

	public String getAuthorName(Long userId) {
		return authorMap.getUsername(userId);
	}

	public void updateAuthor(Context context, Author author) {
		Thread updateAuthor = new UpdateAuthorThread(author);
		updateAuthor.start();
		authorMap = loadFromFile(context, FILENAME);
		Thread searchThread = new SearchThread("");
		searchThread.start();
		saveInFile(context, authorMap, FILENAME);
	}

	class SearchThread extends Thread {
		private String search;

		public SearchThread(String s) {
			search = s;
		}

		@Override
		public void run() {
			authorMap.clear();
			authorMap.putAll(authorMapManager.searchAuthors(search, null, from,
					size, lable));
			saveInFile(context, authorMap, FILENAME);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class AddThread extends Thread {
		private Author newAuthor;

		public AddThread(Author newAuthor) {
			this.newAuthor = newAuthor;
		}

		@Override
		public void run() {
			User.author = newAuthor;
			authorMap.addAuthor(newAuthor);
			saveInFile(context, authorMap, FILENAME);
			authorMapManager.addAuthor(newAuthor);
			// Give some time to get updated info
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * load the author map from the local file
	 * 
	 * @param context
	 *            the context.
	 * @param FILENAME
	 *            the name of the local file.
	 * 
	 * @return authorMap, the author map.
	 */
	public static AuthorMap loadFromFile(Context context, String FILENAME) {
		AuthorMap authorMap = null;
		try {
			FileInputStream fis = context.openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			Gson gson = new Gson();
			// Following line from
			// https://sites.google.com/site/gson/gson-user-guide 2014-09-23
			Type listType = new TypeToken<AuthorMap>() {
			}.getType();
			authorMap = gson.fromJson(in, listType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (authorMap == null)
			return authorMap = new AuthorMap();
		return authorMap;
	}

	/**
	 * save the author map to the local file
	 * 
	 * @param context
	 *            the context.
	 * @param authorMap
	 *            the given author map.
	 * @param FILENAME
	 *            the name of the local file.
	 */
	public static void saveInFile(Context context, AuthorMap authorMap,
			String FILENAME) {
		try {
			FileOutputStream fos = context.openFileOutput(FILENAME, 0);
			Gson gson = new Gson();
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(authorMap, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
