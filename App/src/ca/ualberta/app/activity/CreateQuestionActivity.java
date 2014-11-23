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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.AuthorMapIO;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateAuthorThread;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateQuestionActivity extends Activity {
	private ImageView imageView;
	private EditText titleText = null;
	private EditText contentText = null;
	private Question newQuestion = null;
	private Bitmap image = null;
	private String imageString = null;
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
		titleText = (EditText) findViewById(R.id.question_title_editText);
		contentText = (EditText) findViewById(R.id.question_content_editText);
		imageView = (ImageView) findViewById(R.id.question_image_imageView);
		questionListManager = new QuestionListManager();
		authorMapManager = new AuthorMapManager();
		authorMap = new AuthorMap();
		imageView.setVisibility(View.GONE);
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private static final int GET_IMAGE_ACTIVITY_REQUEST_CODE = 2;

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

	public void select_question_pic(View view) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, GET_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
				String imagePath = imageFileUri.getPath();
				Toast.makeText(this, "Photo OK!", Toast.LENGTH_SHORT).show();
				setImageView(imagePath);
				saveImageView(imagePath);

			}
			if (requestCode == GET_IMAGE_ACTIVITY_REQUEST_CODE) {
				String imagePath = getPath(this, data.getData());
				Toast.makeText(this, "Picture OK!", Toast.LENGTH_SHORT).show();
				setImageView(imagePath);
				saveImageView(imagePath);
			}
		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Photo Canceled!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Have no idea what happened!",
					Toast.LENGTH_SHORT).show();
		}

	}

	// The following code is from
	// http://hmkcode.com/android-display-selected-image-and-its-real-path/
	// 2014-11-18
	private String getPath(Context context, Uri imageFileUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(context, imageFileUri,
				proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;
	}

	private void setImageView(String imagePath) {
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageDrawable(Drawable.createFromPath(imagePath));
	}

	private void saveImageView(String imagePath) {
		image = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 80, stream);
		imageString = Base64.encodeToString(stream.toByteArray(),
				Base64.NO_WRAP);
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
					title, imageString);
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
