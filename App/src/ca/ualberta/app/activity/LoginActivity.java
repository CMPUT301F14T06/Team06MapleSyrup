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
import ca.ualberta.app.network.InternetConnectionChecker;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		usernameEdit = (EditText) findViewById(R.id.username_editText);
		authorMap = new AuthorMap();
		authorMapManager = new AuthorMapManager();
		if (InternetConnectionChecker.isNetworkAvailable(context)) {
			Thread thread = new SearchThread("");
			thread.start();

		} else {
			authorMap = AuthorMapIO.loadFromFile(context, FILENAME);
		}

	}

	public void cancel_login(View view) {
		finish();

	}

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
				// if (InternetConnectionChecker.isNetworkAvailable(context)) {
				//
				// }
				Thread addThread = new AddThread(newAuthor);
				addThread.start();
			}
		}
	}

	private void notifyLogin() {
		Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
	}

	private void notifyAddNewAuthor() {
		Toast.makeText(this, "Login as New Author", Toast.LENGTH_SHORT).show();

	}

	private void notifyNoUsernameEntered() {
		Toast.makeText(this, "Please fill in the username to login",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	/**
	 * Handle action bar item clicks here. The action bar will automatically
	 * handle clicks on the Home/Up button, so long as you specify a parent
	 * activity in AndroidManifest.xml.
	 * 
	 * @param item
	 *            The menu item.
	 * @return true if the item is selected.
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