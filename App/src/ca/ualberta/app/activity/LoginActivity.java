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

import ca.ualberta.app.activity.R;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the activity for the login functionality. Once the "Login" button is
 * clicked or other operations that need author authority are called by a
 * non-author user.
 * 
 * @author Anni
 * @author Bicheng
 */
public class LoginActivity extends Activity {
	private EditText usernameEdit;
	private String username;
	private Context context;
	private AuthorMapController authorMapController;

	public static String LOGINCAUSE;
	private String loginCause;
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			if (loginCause.equals("Question")) {
				Intent intent = new Intent(LoginActivity.this,
						CreateQuestionActivity.class);
				startActivity(intent);
			}
			finish();
		}
	};

	/**
	 * onCreate method Once a user enter this activity, this method will give
	 * each view an object to help other methods set data or listener. Also, a
	 * new thread for the current user will be created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state bundle.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;
		usernameEdit = (EditText) findViewById(R.id.username_editText);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		loginCause = extras.getString(LOGINCAUSE);
		if (loginCause.equals("Upvote"))
			Toast.makeText(context, "Please Login to upvote",
					Toast.LENGTH_SHORT).show();
		else if (loginCause.equals("Question"))
			Toast.makeText(context, "Please Login to ask questions",
					Toast.LENGTH_SHORT).show();
		else if (loginCause.equals("Answer"))
			Toast.makeText(context, "Please Login to answer questions",
					Toast.LENGTH_SHORT).show();
		else if (loginCause.equals("Reply"))
			Toast.makeText(context, "Please Login to reply", Toast.LENGTH_SHORT)
					.show();
	}

	/**
	 * stop the thread when cancel a login operation
	 * 
	 * @param view
	 *            The view.
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
	 *            The view.
	 */
	public void login(View view) {
		authorMapController = new AuthorMapController(context);
		username = usernameEdit.getText().toString().trim();
		if (username.length() == 0) {
			notifyNoUsernameEntered();
		} else {
			User.loginStatus = true;
			if (authorMapController.hasAuthor(context, username)) {
				User.author = authorMapController.getAuthor(username);
				if (InternetConnectionChecker.isNetworkAvailable())
					notifyLogin();
				else
					notifyLoginOffLine();
			} else {
				Author newAuthor = new Author(username);
				authorMapController.addAuthor(context, newAuthor);
				if (InternetConnectionChecker.isNetworkAvailable())
					notifyAddNewAuthor();
				else
					notifyLoginOffLine();
			}
			runOnUiThread(doFinishAdd);
		}
	}

	/**
	 * Notify if the user has login Offline.
	 */
	private void notifyLoginOffLine() {
		Toast.makeText(this, "Login Off-Line", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Notify if the user has login before.
	 */
	private void notifyLogin() {
		Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Notify if the user is a new author.
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
	 * Inflate the menu.
	 * 
	 * @param menu
	 *            The menu.
	 * @return true if the menu is acted.
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

}