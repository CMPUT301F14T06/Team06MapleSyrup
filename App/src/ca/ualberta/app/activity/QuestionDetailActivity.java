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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.AnswerListAdapter;
import ca.ualberta.app.adapter.ReplyListAdapter;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateQuestionThread;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionDetailActivity extends Activity {
	public static String QUESTION_ID = "QUESTION_ID";
	private TextView questionTitleTextView;
	private TextView questionContentTextView;
	private TextView authorNameTextView;
	private TextView questionUpvoteTextView;
	private TextView answerCountTextView;
	private TextView questionTimeTextView;
	private ImageView questionImageView;
	private ExpandableListView question_ReplyListView;
	private ExpandableListView question_AnswerListView;
	private RadioButton answer_Rb;
	private RadioButton reply_Rb;
	private RadioButton fav_Rb;
	private RadioButton save_Rb;
	private long questionId;
	private Question question;
	private QuestionListManager questionManager;
	private CacheController cacheController;
	private ReplyListAdapter replyAdapter = null;
	private AnswerListAdapter answerAdapter = null;
	private Context mcontext;
	private boolean save_click = false;
	private boolean fav_click = false;
	private boolean upvote_click = false;

	private Runnable doUpdateGUIDetails = new Runnable() {
		public void run() {
			if (!(save_click || upvote_click || fav_click)
					&& !cacheController.hasSaved(mcontext, question))
				cacheController.addLocalQuestions(mcontext, question);

			if (cacheController.hasSaved(mcontext, question))
				save_Rb.setChecked(true);
			else
				save_Rb.setChecked(false);

			if (cacheController.hasFavorited(mcontext, question))
				fav_Rb.setChecked(true);
			else
				fav_Rb.setChecked(false);

			questionTitleTextView.setText(question.getTitle());
			questionContentTextView.setText(question.getContent());
			authorNameTextView.setText(question.getAuthor());
			questionUpvoteTextView.setText("Upvote: "
					+ question.getQuestionUpvoteCount());
			answerCountTextView.setText("Answer: " + question.getAnswerCount());
			questionTimeTextView.setText(question.getTimestamp().toString());

			if (question.hasImage()) {
				questionImageView.setVisibility(View.VISIBLE);
				byte[] imageByteArray = Base64.decode(question.getImage(),
						Base64.NO_WRAP);
				Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0,
						imageByteArray.length);
				questionImageView.setImageBitmap(image);
			}

			replyAdapter = new ReplyListAdapter(mcontext,
					R.layout.single_reply, question.getReplys(), question);
			answerAdapter = new AnswerListAdapter(mcontext,
					R.layout.single_answer, R.layout.single_reply,
					question.getAnswers(), question);
			question_AnswerListView.setAdapter(answerAdapter);
			question_ReplyListView.setAdapter(replyAdapter);
			replyAdapter.notifyDataSetChanged();
			answerAdapter.notifyDataSetChanged();
		}
	};

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
		answer_Rb = (RadioButton) findViewById(R.id.question_answer_button);
		reply_Rb = (RadioButton) findViewById(R.id.question_reply_button);
		save_Rb = (RadioButton) findViewById(R.id.save_detail_button);
		fav_Rb = (RadioButton) findViewById(R.id.fav_detail_button);
		cacheController = new CacheController(mcontext);
		questionImageView.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		check_login_status();
	}

	@Override
	protected void onStart() {
		super.onStart();
		check_login_status();
		questionManager = new QuestionListManager();
		Intent intent = getIntent();

		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				questionId = extras.getLong(QUESTION_ID);
				Thread thread = new GetThread(questionId);
				thread.start();
			}
		}

	}

	public void check_login_status() {
		if (User.loginStatus == true) {
			answer_Rb.setVisibility(View.VISIBLE);
			reply_Rb.setVisibility(View.VISIBLE);
		} else {
			answer_Rb.setVisibility(View.GONE);
			reply_Rb.setVisibility(View.GONE);
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
		Thread thread = new GetThread(questionId);
		thread.start();
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
		Thread thread = new GetThread(questionId);
		thread.start();
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
		Thread thread = new GetThread(questionId);
		thread.start();
	}

	/**
	 * This method will be called when the "Add Answer" button is clicked. It
	 * will start a new activity for answering a question.
	 * 
	 * @param view
	 *            The view.
	 */

	public void answer_question(View view) {
		Intent intent = new Intent(this, CreateAnswerActivity.class);
		intent.putExtra(CreateAnswerActivity.QUESTION_ID, questionId);
		startActivity(intent);
	}

	/**
	 * This method will be called when the "Add Reply" button is clicked. It
	 * will start a new activity for replying a question.
	 * 
	 * @param view
	 *            The view.
	 */

	public void reply_question(View view) {
		Intent intent = new Intent(this, CreateQuestionReplyActivity.class);
		intent.putExtra(CreateAnswerActivity.QUESTION_ID, questionId);
		startActivity(intent);
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

		/**
		 * check which list the current question belongs to, and save the
		 * question in different lists.
		 * 
		 */

		@Override
		public void run() {

			question = questionManager.getQuestion(id);

			if (upvote_click == true) {
				if (User.loginStatus) {
					if (!question.upvoteQuestion()) {
						Looper.prepare();
						Toast.makeText(mcontext,
								"You have upvoted this question",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					} else {
						cacheController.updateFavQuestions(mcontext, question);
						cacheController
								.updateLocalQuestions(mcontext, question);
					}
				} else {
					Looper.prepare();
					Toast.makeText(mcontext, "Login to upvote",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mcontext, LoginActivity.class);
					startActivity(intent);
					Looper.loop();
				}
			}
			if (save_click == true) {
				if (cacheController.hasSaved(mcontext, question))
					cacheController.removeLocalQuestions(mcontext, question);
				else
					cacheController.addLocalQuestions(mcontext, question);
			}
			if (fav_click == true) {
				if (cacheController.hasFavorited(mcontext, question))
					cacheController.removeFavQuestions(mcontext, question);
				else
					cacheController.addFavQuestions(mcontext, question);
			}

			Thread updateThread = new UpdateQuestionThread(question);
			updateThread.start();
			runOnUiThread(doUpdateGUIDetails);

		}
	}

}
