package ca.ualberta.app.activity;

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
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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
			if (!(save_click || upvote_click || fav_click))
				cacheController.addLocalQuestions(mcontext, question);
			if (cacheController.hasSaved(question))
				save_Rb.setChecked(true);
			else
				save_Rb.setChecked(false);

			if (cacheController.hasFavorited(question))
				fav_Rb.setChecked(true);
			else
				fav_Rb.setChecked(false);
			save_click = false;
			fav_click = false;
			upvote_click = false;
			questionTitleTextView.setText(question.getTitle());
			questionContentTextView.setText(question.getContent());
			authorNameTextView.setText(question.getAuthor());
			questionUpvoteTextView.setText("Upvote: "
					+ question.getQuestionUpvoteCount());
			answerCountTextView.setText("Answer: " + question.getAnswerCount());
			questionTimeTextView.setText(question.getTimestamp().toString());
			// if (question.getReplys().size() == 0)
			// question_ReplyListView.setVisibility(View.GONE);
			if (question.hasImage()) {
				questionImageView.setVisibility(View.VISIBLE);
				questionImageView.setImageBitmap(question.getImage());
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
		if (User.loginStatus == true) {
			answer_Rb.setVisibility(View.VISIBLE);
			reply_Rb.setVisibility(View.VISIBLE);
		} else {
			answer_Rb.setVisibility(View.GONE);
			reply_Rb.setVisibility(View.GONE);
		}
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
				Thread thread = new GetThread(questionId);
				thread.start();
			}
		}

	}

	public void save_click(View view) {
		save_click = true;
		Thread thread = new GetThread(questionId);
		thread.start();
	}

	public void fav_click(View view) {
		fav_click = true;
		Thread thread = new GetThread(questionId);
		thread.start();
	}

	public void upvote_click(View view) {
		upvote_click = true;
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
			if (upvote_click == true) {
				question.upvoteQuestion();
				cacheController.updateFavQuestions(mcontext, question);
				cacheController.updateLocalQuestions(mcontext, question);
			}
			if (save_click == true) {
				if (cacheController.hasSaved(question))
					cacheController.removeLocalQuestions(mcontext, question);
				else
					cacheController.addLocalQuestions(mcontext, question);
			}
			if (fav_click == true) {
				if (cacheController.hasFavorited(question))
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
