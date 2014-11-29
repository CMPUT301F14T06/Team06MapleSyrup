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

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.PushController;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateQuestionActivity extends Activity {
	private ImageView imageView;
	private EditText titleText = null;
	private EditText contentText = null;
	private Question newQuestion = null;
	private Bitmap image = null;
	private Bitmap imageThumb = null;
	private String imageString = null;
	private PushController pushController;
	private QuestionListManager questionListManager;
	private AuthorMapController authorMapController;

	private CacheController cacheController;
	public static String QUESTION_ID = "QUESTION_ID";
	public static String QUESTION_TITLE = "QUESTION_TITLE";
	public static String QUESTION_CONTENT = "QUESTION_CONTENT";
	public static String IMAGE = "IMAGE";
	Uri imageFileUri;
	Uri stringFileUri;
	private boolean edit = false;
	private long questionID;

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
		pushController = new PushController(this);
		questionListManager = new QuestionListManager();
		authorMapController = new AuthorMapController(this);
		imageView.setVisibility(View.GONE);
		cacheController = new CacheController(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				questionID = extras.getLong(QUESTION_ID);
				String questionTitle = extras.getString(QUESTION_TITLE);
				String questionContent = extras.getString(QUESTION_CONTENT);
				byte[] imageByteArray = Base64.decode(
						extras.getByteArray(IMAGE), Base64.NO_WRAP);
				image = BitmapFactory.decodeByteArray(imageByteArray, 0,
						imageByteArray.length);
				scaleImage(THUMBIMAGESIZE, THUMBIMAGESIZE, true);
				imageView.setVisibility(View.VISIBLE);
				imageView.setImageBitmap(imageThumb);
				titleText.setText(questionTitle);
				contentText.setText(questionContent);
				edit = true;
			}
		}
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
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setType("image/*");
		startActivityForResult(intent, GET_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
				String imagePath = imageFileUri.getPath();

				if (saveImageView(imagePath)) {
					setImageView();
				} else {
					refuseUpdatePic();
				}
			}
			if (requestCode == GET_IMAGE_ACTIVITY_REQUEST_CODE) {

				String imagePath = getPath(this, data.getData());
				if (saveImageView(imagePath)) {
					setImageView();
				} else {
					refuseUpdatePic();
				}
			}
		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Photo Canceled!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Have no idea what happened!",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void refuseUpdatePic() {
		image = null;
		imageThumb = null;
		imageString = null;
		imageView.setVisibility(View.GONE);
		Toast.makeText(
				this,
				"The Photo is larger than 64KB after resize, please choose another one.",
				Toast.LENGTH_LONG).show();
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

	private static final int THUMBIMAGESIZE = 200;
	private static final int SCALEIMAGESIZE = 800;

	private Boolean saveImageView(String imagePath) {
		image = BitmapFactory.decodeFile(imagePath);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		if (stream.toByteArray().length / 1024 > 1024) {
			stream.reset();
			image.compress(Bitmap.CompressFormat.JPEG, 50, stream);
		}
		scaleImage(SCALEIMAGESIZE, SCALEIMAGESIZE, false);
		imageString = compressImage();
		if (imageString == null)
			return false;
		return true;
	}

	private void scaleImage(int width, int height, boolean createThumb) {
		// Scale the pic if it is too large:

		double scaleFactor = 1;
		if (image.getWidth() > width) {
			scaleFactor = image.getWidth() / width;
		} else if (image.getHeight() > height
				&& image.getHeight() > image.getWidth()) {
			scaleFactor = image.getHeight() / height;
		}
		int newWidth = (int) Math.round(image.getWidth() / scaleFactor);
		int newHeight = (int) Math.round(image.getHeight() / scaleFactor);
		if (createThumb)
			imageThumb = Bitmap.createScaledBitmap(image, newWidth, newHeight,
					false);
		else
			image = Bitmap
					.createScaledBitmap(image, newWidth, newHeight, false);

	}

	private String compressImage() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		int quality = 100;
		while (stream.toByteArray().length > 65536 && quality != 0) {
			stream.reset();
			image.compress(Bitmap.CompressFormat.JPEG, quality, stream);
			quality -= 10;
		}
		if (quality == 0)
			return null;
		return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
	}

	private void setImageView() {
		scaleImage(THUMBIMAGESIZE, THUMBIMAGESIZE, true);
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageBitmap(imageThumb);
	}

	public void cancel_question(View view) {
		finish();
	}

	// http://www.csdn123.com/html/mycsdn20140110/2d/2d3c6d5adb428b6708901f7060d31800.html
	public void viewQuestionImage(View view) {
		LayoutInflater inflater = LayoutInflater.from(view.getContext());
		View imgEntryView = inflater.inflate(R.layout.dialog_photo, null);
		final AlertDialog dialog = new AlertDialog.Builder(view.getContext())
				.create();
		ImageView img = (ImageView) imgEntryView.findViewById(R.id.large_image);
		img.setImageBitmap(image);
		dialog.setView(imgEntryView);
		dialog.show();
		imgEntryView.setOnClickListener(new OnClickListener() {
			public void onClick(View paramView) {
				dialog.cancel();
			}
		});
	}

	public void submit_question(View view) {
		String title = titleText.getText().toString();
		String content = contentText.getText().toString();
		if (title.trim().length() == 0)
			noTitleEntered();
		else {
			if (edit == false) {
				newQuestion = new Question(content, User.author.getUserId(),
						title, imageString);
				User.author.addAQuestion(newQuestion.getID());
			} else {
				newQuestion = new Question(content, User.author.getUserId(),
						title, imageString);
				newQuestion.setID(questionID);
			}
			if (InternetConnectionChecker.isNetworkAvailable()) {
				authorMapController.updateAuthor(this, User.author);
				Thread addQuestionThread = new AddQuestionThread(newQuestion);
				addQuestionThread.start();
				cacheController.addMyQuestion(view.getContext(), newQuestion);
			} else {
				if (edit == false) {
					Toast.makeText(
							this,
							"Question added to Waiting List, it will be post when Internet is connected.",
							Toast.LENGTH_LONG).show();
					pushController.addWaitngListQuestions(
							getApplicationContext(), newQuestion);
				} else {

					pushController.updateWaitingListQuestion(
							getApplicationContext(), newQuestion);
				}

				finish();
			}
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

}
