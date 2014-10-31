package ca.ualberta.app.models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AuthorMapIO {
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (authorMap == null)
			return authorMap = new AuthorMap();
		return authorMap;
	}

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
