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

package ca.ualberta.app.activity;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.AuthorMapIO;
import ca.ualberta.app.models.User;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the activity for the login functionality.
 * 
 * @author Anni, Bicheng, Xiaocong
 * 
 */
public class LoginActivity extends Activity {
	private EditText usernameEdit;
	private String username;
	private AuthorMap authorMap;
	private String FILENAME = "AUTHORMAP.sav";
	private Context context;
	private AuthorMapManager authorMapManager;
	private long from = 0;
	private long size = 1000;
	private String lable = "author";
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	/**
	 * onCreate method Once a user enter this activity, this method will give
	 * each view an object to help other methods set data or listener. Also, a
	 * new thread for the current user will be created.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		usernameEdit = (EditText) findViewById(R.id.username_editText);
		authorMap = new AuthorMap();
		authorMapManager = new AuthorMapManager();
		Thread thread = new SearchThread("");
		thread.start();

	}

	/**
	 * Cancel a login action
	 * 
	 * @param view
	 */
	public void cancel_login(View view) {
		finish();

	}

	/**
	 * This method will be called when the user types in a user name and want to
	 * login. It will check if the user name has already exist in the current
	 * data set, and give the user a response.
	 * 
	 * @param view
	 */
	public void login(View view) {
		AuthorMapIO.saveInFile(context, authorMap, FILENAME);
		username = usernameEdit.getText().toString().trim();
		if (username.length() == 0) {
			notifyNoUsernameEntered();
		} else {
			User.loginStatus = true;
			if (authorMap.getMap().get(username) != null) {
				User.author = authorMap.getMap().get(username);
				notifyLogin();
				finish();
			} else {
				Author newAuthor = new Author(username);
				notifyAddNewAuthor();
				Thread addThread = new AddThread(newAuthor);
				addThread.start();
			}
		}
	}

	/**
	 * Notify if the user has login before
	 */
	private void notifyLogin() {
		Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Notify if the user has not login before
	 */
	private void notifyAddNewAuthor() {
		Toast.makeText(this, "Login as New Author", Toast.LENGTH_SHORT).show();

	}

	/**
	 * Notify if the user doesn't enter an user name
	 */
	private void notifyNoUsernameEntered() {
		Toast.makeText(this, "Please fill in the username to login",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Handle action bar item clicks here. The action bar will automatically
	 * handle clicks on the Home/Up button, so long as you specify a parent
	 * activity in AndroidManifest.xml.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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

		}
	}

	/**
	 * If the user is not a new user, find the thread for he/her
	 * 
	 * @author Anni
	 * 
	 */
	class GetThread extends Thread {
		// TODO: Implement search thread
		private String username;

		public GetThread(String username) {
			this.username = username;
		}

		@Override
		public void run() {
			User.author = authorMapManager.getAuthor(username);
		}
	}

	/**
	 * If the user is a new user, add a new thread for he/her
	 * 
	 * @author Anni
	 * 
	 */
	class AddThread extends Thread {
		private Author newAuthor;

		public AddThread(Author newAuthor) {
			this.newAuthor = newAuthor;
		}

		@Override
		public void run() {
			User.author = newAuthor;
			authorMap.addAuthor(newAuthor);
			AuthorMapIO.saveInFile(context, authorMap, FILENAME);
			authorMapManager.addAuthor(newAuthor);
			// Give some time to get updated info
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runOnUiThread(doFinishAdd);
		}
	}
}