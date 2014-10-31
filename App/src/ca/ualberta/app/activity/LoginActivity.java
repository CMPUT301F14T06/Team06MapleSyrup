package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.AuthorMapIO;
import ca.ualberta.app.models.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	ImageButton login_button;
	EditText usernameEdit;
	String username;
	AuthorMap authorMap;
	String FILENAME = "AUTHOR.sav";
	Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		login_button = (ImageButton) findViewById(R.id.login_button);
		usernameEdit = (EditText) findViewById(R.id.username_editText);
		authorMap = AuthorMapIO.loadFromFile(context, FILENAME);

		login_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				username = usernameEdit.getText().toString().trim();
				if (username.trim().length() == 0) {
					notifyNoUsernameEntered();
				} else {
					User.loginStatus = true;
					if (authorMap.addAuthor(username)) {
						User.author = authorMap.getMap().get(username);
						notifyAddNewAuthor();
					} else {
						User.author = authorMap.getMap().get(username);
						notifyLogin();
					}
					AuthorMapIO.saveInFile(context, authorMap, FILENAME);
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
			}
		});
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

}
