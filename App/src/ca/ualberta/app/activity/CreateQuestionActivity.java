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
import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.AuthorMapIO;
import ca.ualberta.app.models.Question;
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
import android.widget.Toast;

public class CreateQuestionActivity extends Activity {
	private RadioButton galary;
	private ImageView image;
	private EditText titleText = null;
	private EditText contentText = null;
	private Question newQuestion = null;
	private Bitmap addImage = null;
	private QuestionListManager questionListManager;
	private AuthorMapManager authorMapManager;
	private String FILENAME = "AUTHORMAP.sav";
	private AuthorMap authorMap;
	private long from = 0;
	private long size = 1000;
	private String lable = "author";
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
		setContentView(R.layout.activity_create_question);
		galary = (RadioButton) findViewById(R.id.add_question_pic);
		titleText = (EditText) findViewById(R.id.question_title_editText);
		contentText = (EditText) findViewById(R.id.question_content_editText);
		image = (ImageView) findViewById(R.id.question_image_imageView);
		questionListManager = new QuestionListManager();
		authorMapManager = new AuthorMapManager();
		authorMap = new AuthorMap();
		image.setVisibility(View.GONE);
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

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

	public void cancel_question(View view) {
		finish();
	}

	public void submit_question(View view) {
		String title = titleText.getText().toString();
		String content = contentText.getText().toString();
		if (title.trim().length() == 0)
			noTitleEntered();
		else {
			newQuestion = new Question(content, User.author.getUsername(),
					title, addImage);
			User.author.addAQuestion(newQuestion.getID());

			Thread updateAuthorThread = new UpdateAuthorThread(User.author);
			updateAuthorThread.start();

			Thread searchAuthorThread = new SearchAuthorThread("");
			searchAuthorThread.start();

			Thread addQuestionThread = new AddQuestionThread(newQuestion);
			addQuestionThread.start();

			AuthorMapIO.saveInFile(view.getContext(), authorMap, FILENAME);

		}
	}


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

	
	class SearchAuthorThread extends Thread {
		private String search;

	
		public SearchAuthorThread(String s) {
			search = s;
		}


		@Override
		public void run() {
			authorMap.clear();
			authorMap.putAll(authorMapManager.searchAuthors(search, null, from,
					size, lable));
		}
	}

}
