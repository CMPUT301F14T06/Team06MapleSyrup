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
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateQuestionThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the activity for the mean functionality of replying answer.
 * This activity will be acted when the "Reply" button in the
 * QuestionDetailActivity.java is clicked.
 * 
 * @author Anni
 * @author Bicheng
 * @author Xiaocong
 *
 */
public class CreateAnswerReplyActivity extends Activity {
	private EditText contentText = null;
	private Reply newReply = null;
	private QuestionListManager questionListManager;
	public static String QUESTION_ID = "QUESTION_ID";
	public static String ANSWER_POS = "ANSWER_POS";
	private Intent intent;
	
	/**
	 * This method will be called when the user finishes replying an answer 
	 * to stop the the current thread.
	 */
	private Runnable doFinishAdd = new Runnable() {
		public void run() {
			finish();
		}
	};

	/**
	 * onCreate method
	 * Once the activity is created, this method will give each view an object
	 * to help other methods set data or listeners.
	 * 
	 * @param savedInstanceState the saved instance state bundle.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_answer_reply);
		contentText = (EditText) findViewById(R.id.answer_reply_editText);
		questionListManager = new QuestionListManager();
		intent = getIntent();
	}

	/**
	 * If the user cancel the current operation, then stop the current thread
	 * 
	 * @param view View passed to the activity to check which button was pressed.
	 */
	public void cancel_answer_reply(View view) {
		finish();
	}

	/**
	 * This method will be called when the current reply is submitted, then map the 
	 * thread to the corresponding question and save all details into the answer of the question.
	 * 
	 * @param view View passed to the activity to check which button was pressed.
	 */
	public void submit_answer_reply(View view) {
		String content = contentText.getText().toString();
		if (content.trim().length() == 0)
			noContentEntered();
		else {
			if (intent != null) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					long questionId = extras.getLong(QUESTION_ID);
					int answerPos = extras.getInt(ANSWER_POS);
					newReply = new Reply(content, User.author.getUsername());
					Thread thread = new GetUpdateThread(questionId, answerPos, newReply);
					thread.start();
				}
			}

		}

	}

	/**
	 * Set the text to mention the user that the current reply need content
	 */
	public void noContentEntered() {
		Toast.makeText(this, "Please fill in the content!", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * initial the menu on the top right corner of the screen
	 * 
	 * @param menu The menu.
	 * @return true if the menu is acted.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_input, menu);
		return true;
	}

	/**
	 * check if the item in the menu is selected
	 * 
	 * @param menu The menu.
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
	 * Add the new reply to the question details, and stop the current thread when everything is done.
	 */
	class GetUpdateThread extends Thread {
		private long id;
		private int pos;
		private Reply newReply;

		/**
		 * the constructor of the class
		 * 
		 * @param id the ID of the current reply.
		 * @param newAnswer the current reply.
		 */
		public GetUpdateThread(long id, int pos, Reply newReply) {
			this.newReply = newReply;
			this.id = id;
			this.pos = pos;
		}

		/**
		 * use the corresponding thread to update the modification online
		 */
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
