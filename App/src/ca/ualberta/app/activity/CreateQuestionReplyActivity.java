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

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.controller.PushController;
import ca.ualberta.app.gps.Location;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.thread.UpdateQuestionThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateQuestionReplyActivity extends Activity {
	private EditText contentText = null;
	private Reply newReply = null;
	private QuestionListManager questionListManager;
	private PushController pushController;
	public static String QUESTION_ID = "QUESTION_ID";
	public static String QUESTION_TITLE = "QUESTION_TITLE";
	public static String REPLY_ID = "REPLY_ID";
	public static String REPLY_CONTENT = "REPLY_CONTENT";
	public static String EDIT_MODE = "EDIT_MODE";
	private Intent intent;
	private boolean addLocation = false;
	private String locationName;
	private double[] locationCoordinates;
	private TextView locationText;
	
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_question_reply);
		contentText = (EditText) findViewById(R.id.question_reply_content_editText);
		locationText = (TextView) findViewById(R.id.questionReplyLocationTextView);
		questionListManager = new QuestionListManager();
		intent = getIntent();
		pushController = new PushController(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				if (extras.getBoolean(EDIT_MODE)) {
					String replyContent = extras.getString(REPLY_CONTENT);
					contentText.setText(replyContent);
				}
			}
		}
	}

	public void addQuestionReplyLocation(View view){
		addLocation = true;
		locationName = Location.getLocationName();
		locationCoordinates = Location.getLocationCoordinates();
		locationText.setText(locationName);
	}
	
	public void submit_reply(View view) {
		String content = contentText.getText().toString();
		if (content.trim().length() == 0)
			noContentEntered();
		else {
			if (intent != null) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					long questionId = extras.getLong(QUESTION_ID);
					String questionTitle = extras.getString(QUESTION_TITLE);
					newReply = new Reply(content, User.author.getUserId());
					newReply.setQuestionID(questionId);
					newReply.setQuestionTitle(questionTitle);
					if (addLocation == true){
						newReply.setLocationName(locationName);
						newReply.setLocationCoordinates(locationCoordinates);
					}
					if (extras.getBoolean(EDIT_MODE)) {
						long replyID = extras.getLong(REPLY_ID);
						newReply.setID(replyID);
					}

					if (InternetConnectionChecker.isNetworkAvailable()) {
						Thread thread = new GetUpdateThread(questionId,
								newReply);
						thread.start();
					} else {
						if (extras.getBoolean(EDIT_MODE)) {
							pushController.updateWaitingListReply(
									getApplicationContext(), newReply);
						} else {
							Toast.makeText(
									this,
									"Reply added to Waiting List, it will be post when Internet is connected.",
									Toast.LENGTH_LONG).show();
							pushController.addWaitngListReplies(
									getApplicationContext(), newReply,
									questionTitle);
						}
						finish();
					}
				}
			}

		}
	}

	public void cancel_reply(View view) {
		finish();
	}

	public void noContentEntered() {
		Toast.makeText(this, "Please fill in the content!", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_input, menu);
		return true;
	}

	/**
	 * check if the item in the menu is selected
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

	class GetUpdateThread extends Thread {
		private long id;
		private Reply newReply;

		public GetUpdateThread(long id, Reply newReply) {
			this.newReply = newReply;
			this.id = id;
		}

		@Override
		public void run() {
			Question question = questionListManager.getQuestion(id);
			question.addReply(newReply);
			Thread updateThread = new UpdateQuestionThread(question);
			updateThread.run();
			runOnUiThread(doFinishAdd);
		}
	}

}
