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

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.AnswerListAdapter;
import ca.ualberta.app.adapter.ReplyListAdapter;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.thread.UpdateQuestionThread;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionDetailActivity extends Activity {
	public static String QUESTION_ID = "QUESTION_ID";
	public static String QUESTION_TITLE = "QUESTION_TITLE";
	public static String CACHE_LIST = "CACHE_LIST";

	private TextView questionTitleTextView;
	private TextView questionContentTextView;
	private TextView authorNameTextView;
	private TextView questionUpvoteTextView;
	private TextView answerCountTextView;
	private TextView questionTimeTextView;
	private ImageView questionImageView;
	private ExpandableListView question_ReplyListView;
	private ExpandableListView question_AnswerListView;
	private RadioButton fav_Rb;
	private RadioButton save_Rb;
	private RadioButton upvote_Rb;
	private String cacheList;
	private long questionId;
	private String questionTitle;
	private Question question;
	private QuestionListManager questionManager;
	private CacheController cacheController;
	private AuthorMapController authorMapController;
	private AuthorMapManager authorMapManager;
	private ReplyListAdapter replyAdapter = null;
	private AnswerListAdapter answerAdapter = null;
	private Bitmap image = null;
	private Bitmap imageThumb = null;
	private Context mcontext;
	private boolean save_click = false;
	private boolean fav_click = false;
	private boolean upvote_click = false;
	private String loginCause = "Upvote";
	private String loginCause1 = "Answer";
	private String loginCause2 = "Reply";

	private Runnable doUpdateGUIDetails = new Runnable() {
		public void run() {
			updateUI();
		}
	};

	private void updateUI() {
		setButtonChecked();
		Long userId = question.getUserId();
		AuthorMap authorMap = authorMapController.getAuthorMap(mcontext);
		questionTitleTextView.setText(question.getTitle());
		questionContentTextView.setText(question.getContent());
		authorNameTextView.setText(authorMap.getUsername(userId));
		questionUpvoteTextView.setText("Upvote: "
				+ question.getQuestionUpvoteCount());
		answerCountTextView.setText("Answer: " + question.getAnswerCount());
		questionTimeTextView.setText(question.getTimestamp().toString());

		if (question.hasImage()) {
			byte[] imageByteArray = Base64.decode(question.getImage(),
					Base64.NO_WRAP);
			image = BitmapFactory.decodeByteArray(imageByteArray, 0,
					imageByteArray.length);
			scaleImage();
			questionImageView.setVisibility(View.VISIBLE);
			questionImageView.setImageBitmap(imageThumb);
		}

		replyAdapter = new ReplyListAdapter(mcontext, R.layout.single_reply,
				question.getReplys(), question);
		answerAdapter = new AnswerListAdapter(mcontext, R.layout.single_answer,
				R.layout.single_reply, question.getAnswers(), question);
		question_AnswerListView.setAdapter(answerAdapter);
		question_ReplyListView.setAdapter(replyAdapter);
		replyAdapter.notifyDataSetChanged();
		answerAdapter.notifyDataSetChanged();
	}

	private void setButtonChecked() {
		if (!(save_click || upvote_click || fav_click)
				&& !cacheController.hasSaved(mcontext, question))
			cacheController.addLocalQuestion(mcontext, question);

		if (cacheController.hasSaved(mcontext, question))
			save_Rb.setChecked(true);
		else
			save_Rb.setChecked(false);

		if (cacheController.hasFavorited(mcontext, question))
			fav_Rb.setChecked(true);
		else
			fav_Rb.setChecked(false);
		if (User.loginStatus)
			if (question.hasUpvotedBy(User.author.getUserId()))
				upvote_Rb.setChecked(true);
			else
				upvote_Rb.setChecked(false);
	}

	private static final int THUMBIMAGESIZE = 100;

	private void scaleImage() {
		// Scale the pic if it is too large:

		if (image.getWidth() > THUMBIMAGESIZE
				|| image.getHeight() > THUMBIMAGESIZE) {
			double scalingFactor = image.getWidth() / THUMBIMAGESIZE;
			if (image.getHeight() > image.getWidth()) {
				scalingFactor = image.getHeight() / THUMBIMAGESIZE;

			}
			int newWidth = (int) Math.round(image.getWidth() / scalingFactor);
			int newHeight = (int) Math.round(image.getHeight() / scalingFactor);
			imageThumb = Bitmap.createScaledBitmap(image, newWidth, newHeight,
					false);
		} else {
			imageThumb = image;
		}

	}

	/**
	 * onCreate method Once this activity is created, this method will give each
	 * view an object to help other methods set data or listener. Then, the
	 * method will check is the current user has logged in. If the login statue
	 * is true, then the user will be able to see and user the "Add Answer"
	 * button. Otherwise, the user cannot see the button.
	 * 
	 * @param savedInstanceState
	 *            The saved instance state bundle.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		mcontext = this;
		questionTitleTextView = (TextView) findViewById(R.id.questionDetailTitleTextView);
		questionContentTextView = (TextView) findViewById(R.id.questionDetailContentTextView);
		authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
		questionUpvoteTextView = (TextView) findViewById(R.id.upvoteStateTextView);
		answerCountTextView = (TextView) findViewById(R.id.answerStateTextView);
		questionTimeTextView = (TextView) findViewById(R.id.questionTimeTextView);
		questionImageView = (ImageView) findViewById(R.id.questionImage);
		question_ReplyListView = (ExpandableListView) findViewById(R.id.question_reply_expanableListView);
		question_AnswerListView = (ExpandableListView) findViewById(R.id.answer_listView);
		save_Rb = (RadioButton) findViewById(R.id.save_detail_button);
		fav_Rb = (RadioButton) findViewById(R.id.fav_detail_button);
		upvote_Rb = (RadioButton) findViewById(R.id.upvote_detail_button);
		questionImageView.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkInternet();
	}

	@Override
	protected void onStart() {
		super.onStart();
		cacheController = new CacheController(mcontext);
		questionManager = new QuestionListManager();
		authorMapController = new AuthorMapController(mcontext);
		authorMapManager = new AuthorMapManager();
		Intent intent = getIntent();

		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				checkInternet();
				questionId = extras.getLong(QUESTION_ID);
				questionTitle = extras.getString(QUESTION_TITLE);
				if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
					Thread thread = new GetThread(questionId);
					thread.start();
				} else {
					cacheList = extras.getString(CACHE_LIST);
					if (cacheList.equals("MYQUESTION")) {
						question = cacheController.getMyQuestionMap(mcontext)
								.get(questionId);
					} else if (cacheList.equals("MYFAVORITE")) {
						question = cacheController.getFavoriteMap(mcontext)
								.get(questionId);
					} else if (cacheList.equals("MYLOCAL")) {
						question = cacheController.getLocalCacheMap(mcontext)
								.get(questionId);
					}
					updateUI();
				}
			}
		}

	}

	/**
	 * 
	 */
	private void checkInternet() {
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			upvote_Rb.setEnabled(true);
		} else {
			upvote_Rb.setEnabled(false);
		}
	}

	/**
	 * Set the boolean of the save statue of the question to True
	 * 
	 * @param view
	 *            The view.
	 */

	public void save_click(View view) {
		fav_click = false;
		upvote_click = false;
		save_click = true;
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			Thread thread = new GetThread(questionId);
			thread.start();
		} else {
			checkFavLocalClick();
			updateUI();
		}
	}

	/**
	 * Set the boolean of the favorite statue of the question to True
	 * 
	 * @param view
	 *            The view.
	 */

	public void fav_click(View view) {
		save_click = false;
		upvote_click = false;
		fav_click = true;
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			Thread thread = new GetThread(questionId);
			thread.start();
		} else {
			checkFavLocalClick();
			updateUI();
		}
	}

	/**
	 * increase the up vote counter of the question to True
	 * 
	 * @param view
	 *            The view.
	 */

	public void upvote_click(View view) {
		save_click = false;
		fav_click = false;
		upvote_click = true;
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			view.setClickable(true);
			Thread thread = new GetThread(questionId);
			thread.start();
		} else {
			view.setClickable(false);
		}
	}

	/**
	 * This method will be called when the "Add Answer" button is clicked. It
	 * will start a new activity for answering a question.
	 * 
	 * @param view
	 *            The view.
	 */

	public void answer_question(View view) {
		save_click = false;
		fav_click = false;
		upvote_click = false;

		if (User.loginStatus) {
			Intent intent = new Intent(this, CreateAnswerActivity.class);
			intent.putExtra(CreateAnswerActivity.QUESTION_ID, questionId);
			intent.putExtra(CreateAnswerActivity.QUESTION_TITLE, questionTitle);
			startActivity(intent);
		} else {
			Intent intent = new Intent(mcontext, LoginActivity.class);
			intent.putExtra(LoginActivity.LOGINCAUSE, loginCause1);
			startActivity(intent);
		}
	}

	/**
	 * This method will be called when the "Add Reply" button is clicked. It
	 * will start a new activity for replying a question.
	 * 
	 * @param view
	 *            The view.
	 */

	public void reply_question(View view) {
		save_click = false;
		fav_click = false;
		upvote_click = false;
		if (User.loginStatus) {
			Intent intent = new Intent(this, CreateQuestionReplyActivity.class);
			intent.putExtra(CreateQuestionReplyActivity.QUESTION_ID, questionId);
			intent.putExtra(CreateQuestionReplyActivity.QUESTION_TITLE,
					questionTitle);
			startActivity(intent);
		} else {
			Intent intent = new Intent(mcontext, LoginActivity.class);
			intent.putExtra(LoginActivity.LOGINCAUSE, loginCause2);
			startActivity(intent);
		}
	}

	// http://www.csdn123.com/html/mycsdn20140110/2d/2d3c6d5adb428b6708901f7060d31800.html
	public void viewQuestionImage(View view) {
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		View imgEntryView = inflater.inflate(R.layout.dialog_photo, null);
		final AlertDialog dialog = new AlertDialog.Builder(mcontext).create();
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

	private void checkFavLocalClick() {
		if (save_click == true) {
			if (cacheController.hasSaved(mcontext, question))
				cacheController.removeLocalQuestions(mcontext, question);
			else
				cacheController.addLocalQuestion(mcontext, question);
		}
		if (fav_click == true) {
			if (cacheController.hasFavorited(mcontext, question))
				cacheController.removeFavQuestions(mcontext, question);
			else
				cacheController.addFavQuestion(mcontext, question);
		}
	}

	class GetThread extends Thread {
		private long id;

		/**
		 * the constructor of the class
		 * 
		 * @param id
		 *            the ID of the question.
		 */

		public GetThread(long id) {
			this.id = id;
		}

		@Override
		public void run() {
			question = questionManager.getQuestion(id);
			authorMapController.clear();
			Thread searchAuthorThread = new SearchAuthorMapThread("");
			searchAuthorThread.run();
			checkFavLocalClick();
			if (upvote_click == true) {
				if (User.loginStatus) {
					question.upvoteQuestion();
					Thread updateThread = new UpdateQuestionThread(question);
					updateThread.start();
					cacheController.updateFavQuestions(mcontext, question);
					cacheController.updateLocalQuestions(mcontext, question);
				} else {
					Intent intent = new Intent(mcontext, LoginActivity.class);
					intent.putExtra(LoginActivity.LOGINCAUSE, loginCause);
					startActivity(intent);

				}
			}

			runOnUiThread(doUpdateGUIDetails);

		}
	}

	class SearchAuthorMapThread extends Thread {
		private String search;

		public SearchAuthorMapThread(String s) {
			search = s;
		}

		@Override
		public void run() {
			authorMapController.clear();
			authorMapController.putAll(authorMapManager.searchAuthors(search,
					null, 0, 1000, "author"));
		}
	}
}
