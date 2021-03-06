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

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.PushController;
import ca.ualberta.app.gps.GeoCoder;
import ca.ualberta.app.gps.Location;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.thread.UpdateQuestionThread;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.TextView;

import android.widget.Toast;

/**
 * This is the activity for the mean functionality of answering a question. This
 * activity will be acted when the "Add Answer" button in the
 * QuestionDetailActivity.java is clicked.
 * 
 * @author Anni
 * @author Bicheng
 * 
 */
public class CreateAnswerActivity extends Activity {
	private ImageView imageView;
	private EditText contentText = null;
	private TextView locationText = null;
	private Answer newAnswer = null;
	private Bitmap image = null;
	private Bitmap imageThumb = null;
	private String imageString = null;
	private RadioButton GPSButton;
	private QuestionListManager questionListManager;
	private PushController pushController;
	private CacheController cacheController;
	public static String QUESTION_ID = "QUESTION_ID";
	public static String QUESTION_TITLE = "QUESTION_TITLE";
	public static String ANSWER_ID = "ANSWER_ID";
	public static String ANSWER_CONTENT = "ANSWER_CONTENT";
	public static String EDIT_MODE = "EDIT_MODE";
	public static String IMAGE = "IMAGE";
	private Intent intent;
	Uri imageFileUri;
	Uri stringFileUri;
	private boolean addLocation = false;
	private String locationName;
	private double[] locationCoordinates;

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
		contentText = (EditText) findViewById(R.id.answer_content_editText);
		imageView = (ImageView) findViewById(R.id.answer_image_imageView);
		locationText = (TextView) findViewById(R.id.answerLocationTextView);
		GPSButton = (RadioButton) findViewById(R.id.add_answer_position);
		questionListManager = new QuestionListManager();
		imageView.setVisibility(View.GONE);
		intent = getIntent();
		pushController = new PushController(this);
		cacheController = new CacheController(this);
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
	 * Will be called when user clicked add Location button, it will pop a
	 * dialog and let user choose the method of location
	 * 
	 * @param view
	 *            View passed to the activity to check which button was pressed.
	 */
	public void addAnswerLocation(View view) {
		if (locationName == null) {
			showDialog();
		} else {
			GPSButton.setChecked(false);
			addLocation = false;
			locationName = null;
			locationCoordinates = null;
			locationText.setText("");
		}
	}

	/**
	 * the dialog allow user to choose get the location through GPS or set it
	 * manually
	 */
	private void showDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Select get Location Method");

		alert.setPositiveButton("Setting Manually",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						GPSButton.setChecked(true);
						addLocation = true;
						showSelectedDialog();
					}

				});
		alert.setNegativeButton("GPS", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				GPSButton.setChecked(true);
				addLocation = true;
				locationName = Location.getLocationName();
				locationCoordinates = Location.getLocationCoordinates();
				locationText.setText(locationName);
			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	/**
	 * the dialog allow user to type in the city manually
	 */
	private void showSelectedDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Set Location Manually");
		alert.setMessage("Enter the closest city");

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String userLocation = input.getEditableText().toString();
				double[] coord = GeoCoder.toLatLong(userLocation.toString());
				if (coord[0] == 0.0 && coord[1] == 0.0) {
					showToast();
				} else {
					cacheController.saveUserCoordinates(coord);
					cacheController.saveUserLocation(GeoCoder.toAddress(
							coord[0], coord[1]));
					locationName = cacheController.getUserLocation();
					locationCoordinates = cacheController.getUserCoordinates();
					locationText.setText(locationName);
				}
			}

		});
		alert.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						GPSButton.setChecked(false);
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	/**
	 * will be called when user type in a city that cannot find
	 */
	public void showToast() {
		Toast.makeText(
				this,
				"Sorry, Cannot find the address you provided. Please Try again!",
				Toast.LENGTH_LONG).show();
	}

	/**
	 * set the image to the imageView
	 * 
	 * @param view
	 *            View passed to the activity to check which button was pressed.
	 */
	// http://www.csdn123.com/html/mycsdn20140110/2d/2d3c6d5adb428b6708901f7060d31800.html
	public void viewAnswerImage(View view) {
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

	@Override
	public void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				if (extras.getBoolean(EDIT_MODE)) {
					String answerContent = extras.getString(ANSWER_CONTENT);
					contentText.setText(answerContent);
					try {
						byte[] imageByteArray = Base64.decode(
								extras.getByteArray(IMAGE), Base64.NO_WRAP);
						image = BitmapFactory.decodeByteArray(imageByteArray,
								0, imageByteArray.length);
						scaleImage(THUMBIMAGESIZE, THUMBIMAGESIZE, true);
						imageView.setVisibility(View.VISIBLE);
						imageView.setImageBitmap(imageThumb);
					} catch (Exception e) {
					}

				}
			}
		}
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
					String questionTitle = extras.getString(QUESTION_TITLE);
					newAnswer = new Answer(content, User.author.getUserId(),
							imageString);
					if (addLocation == true) {
						newAnswer.setLocationName(locationName);
						newAnswer.setLocationCoordinates(locationCoordinates);
					}
					newAnswer.setQuestionID(questionId);
					newAnswer.setQuestionTitle(questionTitle);

					if (extras.getBoolean(EDIT_MODE)) {
						long answerID = extras.getLong(ANSWER_ID);
						newAnswer.setID(answerID);
					}
					if (InternetConnectionChecker.isNetworkAvailable()) {
						Thread thread = new GetUpdateThread(questionId,
								newAnswer);
						thread.start();
					} else {
						if (extras.getBoolean(EDIT_MODE)) {
							pushController.updateWaitingListAnswer(
									getApplicationContext(), newAnswer);
						} else {
							Toast.makeText(
									this,
									"Answer added to Waiting List, it will be post when Internet is connected.",
									Toast.LENGTH_LONG).show();
							pushController.addWaitngListAnswers(
									getApplicationContext(), newAnswer,
									questionTitle);
						}
						finish();
					}
				}
			}

		}
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private static final int GET_IMAGE_ACTIVITY_REQUEST_CODE = 2;

	/**
	 * Create a storage for the picture in the answer
	 * 
	 * @param view
	 *            View passed to the activity to check which button was pressed.
	 */
	public void take_answer_pic(View view) {
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

	public void select_answer_pic(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setType("image/*");
		startActivityForResult(intent, GET_IMAGE_ACTIVITY_REQUEST_CODE);
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

	/**
	 * will be called when the image cannot be compressed to 64KB
	 */
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
	/**
	 * function that user to get the image path
	 * 
	 * @param context
	 *            the context
	 * @param imageFileUri
	 *            the imageFileUri that contains the URL of the image
	 * @return return the imagePath of the image
	 */
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

	/**
	 * get the image from the imagePath and try compress the image to 64kb
	 * 
	 * @param imagePath
	 *            the imagePath
	 * @return return true if imageString is created
	 */
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

	/**
	 * scale the image to fixed width and height
	 * 
	 * @param width
	 *            the fixed width
	 * @param height
	 *            the fixed height
	 * @param createThumb
	 *            single to create a thumb nail of the image
	 */
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

	/**
	 * compress the image to 64kb
	 * 
	 * @return return the imageString after compressed
	 */
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