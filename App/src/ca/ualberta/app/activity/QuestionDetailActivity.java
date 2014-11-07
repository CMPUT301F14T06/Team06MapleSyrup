package ca.ualberta.app.activity;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.AnswerListAdapter;
import ca.ualberta.app.adapter.ReplyListAdapter;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.thread.UpdateQuestionThread;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionDetailActivity extends Activity {
	private String FAVMAP = "favMap.sav";
	private String LOCALMAP = "localMap.sav";
	public static String QUESTION_ID = "QUESTION_ID";
	private TextView questionTitleTextView;
	private TextView questionContentTextView;
	private TextView authorNameTextView;
	private TextView questionUpvoteTextView;
	private TextView answerCountTextView;
	private ImageView questionImageView;
	private ExpandableListView question_ReplyListView;
	private ListView question_AnswerListView;
	private RadioButton answer_Rb;
	private RadioButton reply_Rb;
	private RadioButton fav_Rb;
	private RadioButton save_Rb;
	private long questionId;
	private Question question;
	private QuestionListManager questionManager;
	private QuestionListController questionListController;
	private AnswerListAdapter adapter = null;
	private ReplyListAdapter replyAdapter = null;
	private Context mcontext;
	private boolean upvote = false;

	private Runnable doUpdateGUIDetails = new Runnable() {
		public void run() {
			if (User.localCacheId.get(question.getID()) == null)
				save_Rb.setChecked(false);
			else
				save_Rb.setChecked(true);

			if (User.favoriteId.get(question.getID()) == null)
				fav_Rb.setChecked(false);
			else
				fav_Rb.setChecked(true);

			questionTitleTextView.setText(question.getTitle());
			questionContentTextView.setText(question.getContent());
			authorNameTextView.setText(question.getAuthor());
			questionUpvoteTextView.setText("Upvote: "
					+ question.getQuestionUpvoteCount());
			answerCountTextView.setText("Answer: " + question.getAnswerCount());
			if (question.getReplys().size() == 0)
				question_ReplyListView.setVisibility(View.GONE);
			if (question.hasImage()) {
				questionImageView.setVisibility(View.VISIBLE);
				questionImageView.setImageBitmap(question.getImage());
			}
			adapter = new AnswerListAdapter(mcontext, R.layout.single_answer,
					question.getAnswers(), question);
			replyAdapter = new ReplyListAdapter(mcontext,
					R.layout.single_reply, question.getReplys(), question);
			question_AnswerListView.setAdapter(adapter);
			question_ReplyListView.setAdapter(replyAdapter);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		mcontext = this;
		questionListController = new QuestionListController();
		questionTitleTextView = (TextView) findViewById(R.id.questionDetailTitleTextView);
		questionContentTextView = (TextView) findViewById(R.id.questionDetailContentTextView);
		authorNameTextView = (TextView) findViewById(R.id.authorNameTextView);
		questionUpvoteTextView = (TextView) findViewById(R.id.upvoteStateTextView);
		answerCountTextView = (TextView) findViewById(R.id.answerStateTextView);
		questionImageView = (ImageView) findViewById(R.id.questionImage);
		question_ReplyListView = (ExpandableListView) findViewById(R.id.question_reply_expanableListView);
		question_AnswerListView = (ListView) findViewById(R.id.answer_listView);
		answer_Rb = (RadioButton) findViewById(R.id.question_answer_button);
		reply_Rb = (RadioButton) findViewById(R.id.question_reply_button);
		save_Rb = (RadioButton) findViewById(R.id.save_detail_button);
		fav_Rb = (RadioButton) findViewById(R.id.fav_detail_button);
		questionImageView.setVisibility(View.GONE);
		if (User.loginStatus == true) {
			answer_Rb.setVisibility(View.VISIBLE);
			reply_Rb.setVisibility(View.VISIBLE);
		} else {
			answer_Rb.setVisibility(View.GONE);
			reply_Rb.setVisibility(View.GONE);
		}

		question_ReplyListView
				.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
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
				questionId = extras.getLong(QUESTION_ID);
				Toast.makeText(this, "ID: " + questionId, Toast.LENGTH_SHORT)
						.show();
				Thread thread = new GetThread(questionId);
				thread.start();
			}
		}

	}

	public void save_click(View view) {
		long questionId = question.getID();
		if (User.localCacheId.get(questionId) == null) {
			save_Rb.setChecked(true);
		} else {
			save_Rb.setChecked(false);
			User.localCacheId.remove(questionId);
		}
		CacheController.saveInFile(mcontext, User.localCacheId, LOCALMAP);
		Thread thread = new GetThread(questionId);
		thread.start();
	}

	public void fav_click(View view) {
		long questionId = question.getID();
		if (User.favoriteId.get(questionId) == null) {
			fav_Rb.setChecked(true);
		} else {
			fav_Rb.setChecked(false);
			User.favoriteId.remove(questionId);
		}
		CacheController.saveInFile(mcontext, User.favoriteId, LOCALMAP);
		Thread thread = new GetThread(questionId);
		thread.start();
	}

	public void upvote_click(View view) {
		upvote = true;
		Thread thread = new GetThread(questionId);
		thread.start();
	}

	public void answer_question(View view) {
		Intent intent = new Intent(this, CreateAnswerActivity.class);
		intent.putExtra(CreateAnswerActivity.QUESTION_ID, questionId);
		startActivity(intent);
	}

	public void reply_question(View view) {
		Intent intent = new Intent(this, CreateQuestionReplyActivity.class);
		intent.putExtra(CreateAnswerActivity.QUESTION_ID, questionId);
		startActivity(intent);
	}

	class GetThread extends Thread {
		private long id;

		public GetThread(long id) {
			this.id = id;
		}

		@Override
		public void run() {
			question = questionManager.getQuestion(id);
			if (upvote == true) {
				question.upvoteQuestion();
				upvote = false;
			}
			CacheController.updateFavQuestions(mcontext, FAVMAP, question);
			CacheController.updateLocalQuestions(mcontext, LOCALMAP, question);
			Thread updateThread = new UpdateQuestionThread(question);
			updateThread.start();
			runOnUiThread(doUpdateGUIDetails);
		}
	}

}
