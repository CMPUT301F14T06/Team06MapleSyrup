package ca.ualberta.app.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMapManager;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListManager;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateAuthorThread;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateInputsActivity extends Activity {
	private RadioButton galary;
	private ImageView image;
	private EditText titleText = null;
	private EditText contentText = null;
	private Question newQuestion = null;
	private Bitmap testImage = null;
	private ArrayList<Long> myQuestionId;
	private String MYQUESTION = User.author.getUsername() + ".sav";
	private QuestionListManager questionListManager;
	private AuthorMapManager authorMapManager;
	private QuestionListController myQuestionList;
	private Question question;
	Uri imageFileUri;
	Uri stringFileUri;

	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_inputs);
		galary = (RadioButton) findViewById(R.id.add_pic);
		titleText = (EditText) findViewById(R.id.title_editText);
		contentText = (EditText) findViewById(R.id.content_editText);
		image = (ImageView) findViewById(R.id.addImage_imageView);
		questionListManager = new QuestionListManager();
		authorMapManager = new AuthorMapManager();
		myQuestionList = new QuestionListController();
		image.setVisibility(View.GONE);
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	public void take_pic(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// Create a folder to store pictures
		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		// Create an URI for the picture file
		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// When the result is OK, set text "Photo OK!" in the status
		// and set the image in the Button with:
		// button.setImageDrawable(Drawable.createFromPath(imageFileUri.getPath()));
		// When the result is CANCELLED, set text "Photo canceled" in the status
		// Otherwise, set text "Not sure what happened!" with the resultCode

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Photo OK!", Toast.LENGTH_SHORT).show();

				image.setVisibility(View.VISIBLE);
				image.setImageDrawable(Drawable.createFromPath(imageFileUri
						.getPath()));
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Photo Canceled!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this, "Have no idea what happened!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void cancel(View view) {
		finish();
	}

	public void submit(View view) {
		String title = titleText.getText().toString();
		String content = contentText.getText().toString();
		if (title.trim().length() == 0)
			noTitleEntered();
		else {
			newQuestion = new Question(content, User.author.getUsername(),
					title, testImage);
			User.author.addAQuestion(newQuestion.getID());
			// myQuestion = User.author.getAuthorQuestion();
			// QuestionListController.saveInFile(getApplicationContext(),
			// myQuestionMap, MYQUESTION);
			myQuestionList.clear();
			for (long questionId : User.author.getAuthorQuestionId()) {
				Thread getThread = new GetQuestionThread(questionId);
				getThread.start();
				myQuestionList.addQuestion(question);
			}
			QuestionListController.saveInFile(view.getContext(),
					myQuestionList.getQuestionList(), MYQUESTION);
			Thread thread = new UpdateAuthorThread(User.author);
			thread.start();
			thread = new AddQuestionThread(newQuestion);
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

	class AddQuestionThread extends Thread {
		private Question question;

		public AddQuestionThread(Question question) {
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

	class GetQuestionThread extends Thread {
		// TODO: Implement search thread
		private long questionId;

		public GetQuestionThread(long questionId) {
			this.questionId = questionId;
		}

		@Override
		public void run() {
			question = questionListManager.getQuestion(questionId);

		}
	}

}
