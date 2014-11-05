package ca.ualberta.app.activity;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.models.Question;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionDetailActivity extends Activity implements
		OnChildClickListener {

	public static String QUESTION_ID = "QUESTION_ID";
	private ExpandableListView questionReply;
	private ExpandableListView answerReply;
	private TextView questionTitle;
	private TextView questionContent;
	private TextView authorName;
	private TextView questionUpvote;
	private TextView answerCount;
	private ImageView questionImageView;

	private QuestionListManager questionManager;
	private Question question;

	private Runnable doUpdateGUIDetails = new Runnable() {
		public void run() {
			questionTitle = (TextView) findViewById(R.id.questionDetailTitleTextView);
			questionContent = (TextView) findViewById(R.id.questionDetailContentTextView);
			authorName = (TextView) findViewById(R.id.authorNameTextView);
			questionUpvote = (TextView) findViewById(R.id.upvoteStateTextView);
			answerCount = (TextView) findViewById(R.id.answerStateTextView);
			questionImageView = (ImageView) findViewById(R.id.questionImage);

			questionTitle.setText(question.getTitle());
			questionContent.setText(question.getContent());
			authorName.setText(question.getAuthor());
			questionUpvote.setText("Upvote: "
					+ String.valueOf(question.getQuestionUpvoteCount()));
			answerCount.setText("Answer: "
					+ String.valueOf(question.getAnswerCount()));
			questionImageView.setImageBitmap(question.getImage());

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
	}

	@Override
	protected void onStart() {
		super.onStart();

		questionManager = new QuestionListManager();
		Intent intent = getIntent();

		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				long questionId = extras.getLong(QUESTION_ID);
				Toast.makeText(this, "ID: " + questionId, Toast.LENGTH_SHORT)
						.show();
				Thread thread = new GetThread(questionId);
				thread.start();
			}
		}
	}

	class GetThread extends Thread {
		private long id;

		public GetThread(long id) {
			this.id = id;
		}

		@Override
		public void run() {
			question = questionManager.getQuestion(id);

			runOnUiThread(doUpdateGUIDetails);
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
