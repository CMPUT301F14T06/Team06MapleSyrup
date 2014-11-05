package ca.ualberta.app.activity;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.AnswerListAdapter;
import ca.ualberta.app.controller.QuestionListController;
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

public class QuestionDetailActivity extends Activity {

	public static String QUESTION_ID = "QUESTION_ID";
	private TextView questionTitleTextView;
	private TextView questionContentTextView;
	private TextView authorNameTextView;
	private TextView questionUpvoteTextView;
	private TextView answerCountTextView;
	private ImageView questionImageView;
	private ExpandableListView question_ReplyListView;
	private ListView question_AnswerListView;
	
	private Question question;
	private QuestionListManager questionManager;
	private QuestionListController questionListController;
	private AnswerListAdapter adapter = null;


	private Runnable doUpdateGUIDetails = new Runnable() {
		public void run() {
			questionTitleTextView.setText(question.getTitle());
			questionContentTextView.setText(question.getContent());
			authorNameTextView.setText(question.getAuthor());
			questionUpvoteTextView.setText("Upvote: "
					+ String.valueOf(question.getQuestionUpvoteCount()));
			answerCountTextView.setText("Answer: "
					+ String.valueOf(question.getAnswerCount()));
			questionImageView.setImageBitmap(question.getImage());
			//adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		
		questionListController = new QuestionListController();
		questionTitleTextView = (TextView) findViewById(R.id.questionDetailTitleTextView);
		questionContentTextView = (TextView) findViewById(R.id.questionDetailContentTextView);
		authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
		questionUpvoteTextView = (TextView) findViewById(R.id.upvoteStateTextView);
		answerCountTextView = (TextView) findViewById(R.id.answerStateTextView);
		questionImageView = (ImageView) findViewById(R.id.questionImage);
		question_ReplyListView = (ExpandableListView) findViewById(R.id.question_reply_expanableListView);
		question_AnswerListView = (ListView) findViewById(R.id.answer_listView);
//		adapter = new AnswerListAdapter(this, R.layout.single_answer,question.getAnswers(),question);
//	
//		question_AnswerListView.setAdapter(adapter);
		
		question_ReplyListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
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

}
