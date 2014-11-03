package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.AuthorMapIO;
import ca.ualberta.app.models.AuthorMapManager;
import ca.ualberta.app.models.User;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText usernameEdit;
	String username;
	AuthorMap authorMap;
	String FILENAME = "AUTHORMAP.sav";
	Context context = this;
	AuthorMapManager authorMapManager;

	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		usernameEdit = (EditText) findViewById(R.id.username_editText);
		authorMap = new AuthorMap();
		authorMapManager = new AuthorMapManager();
		Thread thread = new SearchThread("");
		thread.start();
	}

	public void login(View view) {
		AuthorMapIO.saveInFile(context, authorMap, FILENAME);
		username = usernameEdit.getText().toString().trim();
		if (username.length() == 0) {
			notifyNoUsernameEntered();
		} else {
			// Thread getThread = new GetThread(username);
			// getThread.start();
			User.loginStatus = true;
			if (authorMap.getMap().get(username) != null) {
				// if (User.author != null) {
				User.author = authorMap.getMap().get(username);
				notifyLogin();
				finish();
			} else {
				notifyAddNewAuthor();
				Author newAuthor = new Author(username);
				Thread addThread = new AddThread(newAuthor);
				addThread.start();
			}
		}
	}

	private void notifyLogin() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
	}

	private void notifyAddNewAuthor() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Login as New Author", Toast.LENGTH_SHORT).show();
	}

	private void notifyNoUsernameEntered() {
		Toast.makeText(this, "Please fill in the username to login",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class SearchThread extends Thread {
		// TODO: Implement search thread
		private String search;

		public SearchThread(String s) {
			search = s;
		}

		@Override
		public void run() {

			authorMap.clear();
			authorMap.putAll(authorMapManager.searchAuthors(search, null));
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
