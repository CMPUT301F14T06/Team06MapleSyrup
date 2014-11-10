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

import java.io.File;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateQuestionThread;
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
import android.widget.Toast;

/**
 * This is the activity for the mean functionality of answering a question. This
 * activity will be acted when the "Add Answer" button in the
 * QuestionDetailActivity.java is clicked.
 */
public class CreateAnswerActivity extends Activity {
	private RadioButton galary;
	private ImageView image;
	private EditText contentText = null;
	private Answer newAnswer = null;
	private Bitmap addImage = null;
	private QuestionListManager questionListManager;
	public static String QUESTION_ID = "QUESTION_ID";
	private Intent intent;
	Uri imageFileUri;
	Uri stringFileUri;

	/**
	 * This method will be called when the user finishes answering a question to
	 * stop the the current thread.
	 */
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	/**
	 * onCreate method Once the activity is created, this method will give each
	 * view an object to help other methods set data or listeners.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_answer);
		galary = (RadioButton) findViewById(R.id.add_answer_pic);
		contentText = (EditText) findViewById(R.id.answer_content_editText);
		image = (ImageView) findViewById(R.id.answer_image_imageView);
		questionListManager = new QuestionListManager();
		image.setVisibility(View.GONE);
		intent = getIntent();
	}

	/**
	 * If the user cancel the current operation, then stop the current thread
	 * 
	 * @param view
	 *            View passed to the activity to check which button was pressed.
	 */
	public void cancel_answer(View view) {
		finish();
	}

	/**
	 * This method will be called when the current answer is submitted, then map
	 * the thread to the corresponding question and save all details into the
	 * question.
	 * 
	 * @param view
	 *            View passed to the activity to check which button was pressed.
	 */
	public void submit_answer(View view) {
		String content = contentText.getText().toString();
		if (content.trim().length() == 0)
			noContentEntered();
		else {
			if (intent != null) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					long questionId = extras.getLong(QUESTION_ID);
					newAnswer = new Answer(content, User.author.getUsername(),
							addImage);
					Thread thread = new GetUpdateThread(questionId, newAnswer);
					thread.start();
				}
			}

		}
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	/**
	 * Create a storage for the picture in the answer
	 * 
	 * @param view
	 *            View passed to the activity to check which button was pressed.
	 */
	public void take_question_pic(View view) {
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

	/**
	 * Display the selected photo in the answer, and notify the user if the
	 * operation is successful
	 * 
	 * @param requestCode
	 *            A code that represents the activity of adding an image.
	 * @param resultCode
	 *            A code that represent if the image adding process is
	 *            committed/canceled.
	 * @param Intent
	 *            data The data.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

	/**
	 * Set the text to mention the user that the current answer need content
	 */
	public void noContentEntered() {
		Toast.makeText(this, "Please fill in the content!", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * initial the menu on the top right corner of the screen
	 * 
	 * @param menu
	 *            The menu.
	 * @return true if the menu is acted.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_input, menu);
		return true;
	}

	/**
	 * Handle action bar item clicks here. The action bar will automatically
	 * handle clicks on the Home/Up button, so long as you specify a parent
	 * activity in AndroidManifest.xml.
	 * 
	 * @param menu
	 *            The menu.
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

	/**
	 * Add the new answer to the question details, and stop the current thread
	 * when everything is done.
	 */
	class GetUpdateThread extends Thread {
		private long id;
		private Answer newAnswer;

		/**
		 * the constructor of the class
		 * 
		 * @param id
		 *            the ID of the current answer.
		 * @param newAnswer
		 *            the current answer.
		 */
		public GetUpdateThread(long id, Answer newAnswer) {
			this.newAnswer = newAnswer;
			this.id = id;
		}

		/**
		 * use the corresponding thread to update the current answer online
		 */
		@Override
		public void run() {
			Question question = questionListManager.getQuestion(id);
			question.addAnswer(newAnswer);
			Thread updateThread = new UpdateQuestionThread(question);
			updateThread.run();
			runOnUiThread(doFinishAdd);
		}
	}

}