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
	private Question newContent = null;
	private Bitmap testImage = null;
	private QuestionList questionList;
	private QuestionList myQuestionList;
	private String FILENAME = "questionList.sav";
	private String MYQUESTION;

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

		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String title = titleText.getText().toString();
				String content = contentText.getText().toString();
				if (title.trim().length() == 0)
					noTitleEntered();
				else {
					questionList = QuestionListController.loadFromFile(
							getApplicationContext(), FILENAME);
					myQuestionList = QuestionListController.loadFromFile(
							getApplicationContext(), MYQUESTION);
					newContent = new Question(content, User.author
							.getUsername(), title, testImage);
					questionList.addQuestion(newContent);
					QuestionListController.saveInFile(getApplicationContext(),
							questionList, FILENAME);
					QuestionListController.saveInFile(getApplicationContext(),
							myQuestionList, MYQUESTION);
					Intent intent = new Intent(CreateInputsActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CreateInputsActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

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
	//
	// private void noContentEntered() {
	// Toast.makeText(this, "Please fill in the Content",
	// Toast.LENGTH_SHORT).show();
	// }
}
