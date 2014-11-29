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
import ca.ualberta.app.models.Answer;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAnswerReplyActivity extends Activity {
	private EditText contentText = null;
	private Reply newReply = null;
	private QuestionListManager questionListManager;
	private PushController pushController;
	public static String QUESTION_ID = "QUESTION_ID";
	public static String ANSWER_ID = "ANSWER_ID";
	public static String QUESTION_TITLE = "QUESTION_TITLE";
	public static String REPLY_ID = "REPLY_ID";
	public static String REPLY_CONTENT = "REPLY_CONTENT";
	public static String EDIT_MODE = "EDIT_MODE";
	public static String ANSWER_POS = "ANSWER_POS";
	private Intent intent;
	private boolean addLocation = false;
	private String locationName;
	private double[] locationCoordinates;
	private TextView locationText;
	private RadioButton GPSButton;


	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_answer_reply);
		contentText = (EditText) findViewById(R.id.answer_reply_editText);
		locationText = (TextView) findViewById(R.id.answerReplyLocationTextView);
		GPSButton = (RadioButton) findViewById(R.id.add_answer_reply_position);
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
	
	public void addAnswerReplyLocation(View view){
		if(locationName == null){
			GPSButton.setChecked(true);
			addLocation = true;
			locationName = Location.getLocationName();
			locationCoordinates = Location.getLocationCoordinates();
			locationText.setText(locationName);
		}
		else{
			GPSButton.setChecked(false);
			addLocation = false;
			locationName = null;
			locationCoordinates = null;
			locationText.setText("");
		}
	}
	
	public void submit_answer_reply(View view) {
		String content = contentText.getText().toString();
		if (content.trim().length() == 0)
			noContentEntered();
		else {
			if (intent != null) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					long questionId = extras.getLong(QUESTION_ID);
					long answerID = extras.getLong(ANSWER_ID);
					String questionTitle = extras.getString(QUESTION_TITLE);
					int answerPos = 0;
					if (!extras.getBoolean(EDIT_MODE)) {
						answerPos = extras.getInt(ANSWER_POS);
					}
					newReply = new Reply(content, User.author.getUserId());
					newReply.setQuestionID(questionId);
					newReply.setAnswerID(answerID);
					newReply.setQuestionTitle(questionTitle);
					if(addLocation == true){
						newReply.setLocationName(locationName);
						newReply.setLocationCoordinates(locationCoordinates);
					}
					if (extras.getBoolean(EDIT_MODE)) {
						long replyID = extras.getLong(REPLY_ID);
						newReply.setID(replyID);
					}
					if (InternetConnectionChecker.isNetworkAvailable()) {
						Thread thread = new GetUpdateThread(questionId,
								answerPos, newReply);
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

	public void cancel_answer_reply(View view) {
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
		private int pos;
		private Reply newReply;

		public GetUpdateThread(long id, int pos, Reply newReply) {
			this.newReply = newReply;
			this.id = id;
			this.pos = pos;
		}

		@Override
		public void run() {
			Question question = questionListManager.getQuestion(id);
			Answer answer = question.getAnswers().get(pos);
			answer.addReply(newReply);
			question.updateAnswer(answer);
			Thread updateThread = new UpdateQuestionThread(question);
			updateThread.run();
			runOnUiThread(doFinishAdd);
		}
	}

}
