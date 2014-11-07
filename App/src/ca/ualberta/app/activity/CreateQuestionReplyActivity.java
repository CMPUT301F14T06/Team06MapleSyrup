package ca.ualberta.app.activity;

import ca.ualberta.app.ESmanager.QuestionListManager;
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

public class CreateQuestionReplyActivity extends Activity {
	private EditText contentText = null;
	private Reply newReply = null;
	private QuestionListManager questionListManager;
	public static String QUESTION_ID = "QUESTION_ID";
	private Intent intent;

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
		questionListManager = new QuestionListManager();
		intent = getIntent();
	}

	public void cancel_reply(View view) {
		finish();
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
					Toast.makeText(this, "ID: " + questionId,
							Toast.LENGTH_SHORT).show();
					newReply = new Reply(content, User.author.getUsername());
					Thread thread = new GetUpdateThread(questionId, newReply);
					thread.start();
				}
			}

		}
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
