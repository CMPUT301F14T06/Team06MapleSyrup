package ca.ualberta.app.activity;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.AnswerWaitingListAdapter;
import ca.ualberta.app.adapter.QuestionWaitingListAdapter;
import ca.ualberta.app.adapter.ReplyWaitingListAdapter;
import ca.ualberta.app.controller.PushController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.view.ScrollListView;
import ca.ualberta.app.view.ScrollListView.IXListViewListener;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class WaitingListActivity extends Activity {

	private QuestionWaitingListAdapter questionAdapter;
	private AnswerWaitingListAdapter answerAdapter;
	private ReplyWaitingListAdapter replyAdapter;
	private QuestionListController waitingQuestionListController;
	private QuestionListManager waitingQuestionListManager;
	private QuestionList waitingQuestionList;
	private PushController pushController;
	private Spinner typeOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spinAdapter;
	private ArrayList<Long> waitingListId;
	private static long categoryID;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	private ArrayList<Answer> answerList;
	private ArrayList<Reply> replyList;

	private String QuestionType = "Question";
	private String AnswerType = "Answer";
	private String ReplyType = "Reply";
	private String[] typeOption = { QuestionType, AnswerType, ReplyType };
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			questionAdapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_list);
		typeOptionSpinner = (Spinner) findViewById(R.id.waiting_List_type_spinner);
		mListView = (ScrollListView) findViewById(R.id.waiting_List_ListView);
		mListView.setPullLoadEnable(false);
		mHandler = new Handler();
		mcontext = this;
	}

	@Override
	public void onStart() {
		super.onStart();
		answerList = new ArrayList<Answer>();
		replyList = new ArrayList<Reply>();
		pushController = new PushController(mcontext);
		waitingQuestionListController = new QuestionListController();
		waitingQuestionListManager = new QuestionListManager();
		questionAdapter = new QuestionWaitingListAdapter(mcontext,
				R.layout.single_waiting_question,
				waitingQuestionListController.getQuestionArrayList());
		answerAdapter = new AnswerWaitingListAdapter(mcontext,
				R.layout.single_waiting_answer, answerList);
		replyAdapter = new ReplyWaitingListAdapter(mcontext,
				R.layout.single_waiting_reply, replyList);
		spinAdapter = new ArrayAdapter<String>(mcontext, R.layout.spinner_item,
				typeOption);
		mListView.setAdapter(questionAdapter);
		typeOptionSpinner.setAdapter(spinAdapter);
		typeOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		updateList();

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionID = waitingQuestionListController.getQuestion(
						pos - 1).getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);
				startActivity(intent);
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Question question = waitingQuestionListController
						.getQuestion(position - 1);
				if (User.author != null
						&& User.author.getUsername().equals(
								question.getAuthor())) {
					Toast.makeText(mcontext,
							"Deleting the Question: " + question.getTitle(),
							Toast.LENGTH_LONG).show();
					Thread thread = new DeleteThread(question.getID());
					thread.start();
				} else {
					Toast.makeText(mcontext,
							"Only Author to the Question can delete",
							Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});

		mListView.setScrollListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						questionAdapter.notifyDataSetChanged();
						onLoad();
					}
				}, 2000);
			}

			@Override
			public void onLoadMore() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						questionAdapter.notifyDataSetChanged();
						onLoad();
					}
				}, 2000);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		QuestionType = "Question(" + waitingQuestionListController.size() + ")";
		AnswerType = "Answer(" + answerList.size() + ")";
		ReplyType = "Reply(" + replyList.size() + ")";
		typeOptionSpinner.setAdapter(spinAdapter);
	}

	private void onLoad() {
		timestamp = new Date();
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(timestamp.toString());
	}

	private void updateList() {
		waitingListId = pushController.getWaitingListId(mcontext);
		if (waitingListId.size() == 0)
			Toast.makeText(mcontext, "No Question are Waiting.",
					Toast.LENGTH_LONG).show();

		if (InternetConnectionChecker.isNetworkAvailable(this)) {
			Thread thread = new postListThread();
			thread.start();
		} else {
			waitingQuestionListController.clear();
			waitingQuestionList = pushController
					.getWaitingQuestionList(mcontext);
			waitingQuestionListController.addAll(waitingQuestionList);
			questionAdapter.notifyDataSetChanged();
		}

	}

	private class change_category_click implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			categoryID = position;
			// Question
			if (categoryID == 0) {
				mListView.setAdapter(questionAdapter);
			}

			// Answer
			if (categoryID == 1) {
				mListView.setAdapter(answerAdapter);
			}

			// Reply
			if (categoryID == 2) {
				mListView.setAdapter(replyAdapter);
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			typeOptionSpinner.setSelection(0);
		}
	}

	class postListThread extends Thread {

		@Override
		public void run() {
			waitingQuestionListController.clear();
			waitingQuestionList = pushController
					.getWaitingQuestionList(mcontext);
			waitingQuestionListManager.addQuestionList(waitingQuestionList);
			runOnUiThread(doUpdateGUIList);
		}
	}

	class DeleteThread extends Thread {
		private long questionID;

		public DeleteThread(long questionID) {
			this.questionID = questionID;
		}

		@Override
		public void run() {
			waitingQuestionListManager.deleteQuestion(questionID);
			// Remove movie from local list
			for (int i = 0; i < waitingQuestionListController.size(); i++) {
				Question q = waitingQuestionListController.getQuestion(i);
				if (q.getID() == questionID) {
					waitingQuestionListController.removeQuestion(i);
					break;
				}
			}
			runOnUiThread(doUpdateGUIList);
		}
	}

	/**
	 * Handle action bar item clicks here. The action bar will automatically
	 * handle clicks on the Home/Up button, so long as you specify a parent
	 * activity in AndroidManifest.xml.
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
}
