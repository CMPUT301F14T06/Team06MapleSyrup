package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListManager;
import ca.ualberta.app.models.User;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CreateInputsActivity extends Activity {
	private RadioGroup create_menu_Rg;
	private RadioButton submit;
	private RadioButton cancel;
	private RadioButton galary;
	private RadioButton photo;
	private EditText titleText = null;
	private EditText contentText = null;
	private Question newQuestion = null;
	private Bitmap testImage = null;
	private QuestionList myQuestionList;
	private String MYQUESTION;
	private QuestionListManager questionListManager;
	
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_inputs);
		create_menu_Rg = (RadioGroup) findViewById(R.id.create_input_menu);
		submit = (RadioButton) findViewById(R.id.submit_button);
		cancel = (RadioButton) findViewById(R.id.cancel_button);
		photo = (RadioButton) findViewById(R.id.take_pic);
		galary = (RadioButton) findViewById(R.id.add_pic);
		titleText = (EditText) findViewById(R.id.title_editText);
		contentText = (EditText) findViewById(R.id.content_editText);
		MYQUESTION = User.author.getUsername() + ".sav";
		questionListManager = new QuestionListManager();
		
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CreateInputsActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

	}
	
	public void submit(View view){
		String title = titleText.getText().toString();
		String content = contentText.getText().toString();
		if (title.trim().length() == 0)
			noTitleEntered();
		else {
			myQuestionList = QuestionListController.loadFromFile(
					getApplicationContext(), MYQUESTION);
			newQuestion = new Question(content, User.author
					.getUsername(), title, testImage);
			myQuestionList.addQuestion(newQuestion);
			
			QuestionListController.saveInFile(getApplicationContext(),
					myQuestionList, MYQUESTION);
			
			
			Thread thread = new AddThread(newQuestion);
			thread.start();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_input, menu);
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

	private void noTitleEntered() {
		Toast.makeText(this, "Please fill in the Title", Toast.LENGTH_SHORT)
				.show();
	}

	class AddThread extends Thread {
		private Question question;

		public AddThread(Question question) {
			this.question = question;
		}

		@Override
		public void run() {
			questionListManager.addQuestion(question);
			
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
