package ca.ualberta.app.activity;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
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
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };
	private QuestionListAdapter adapter;
	private QuestionListController waitingQuestionListController;
	private QuestionListManager waitingQuestionListManager;
	private QuestionList waitingQuestionList;
	private CacheController cacheController;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spinAdapter;
	private ArrayList<Long> waitingListId;
	private static long categoryID;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;

	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
			spinAdapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting_list);
		sortOptionSpinner = (Spinner) findViewById(R.id.waiting_List_sort_spinner);
		mListView = (ScrollListView) findViewById(R.id.waiting_List_ListView);
		mListView.setPullLoadEnable(false);
		mHandler = new Handler();
		mcontext = this;
	}

	@Override
	public void onStart() {
		super.onStart();
		cacheController = new CacheController(mcontext);
		waitingQuestionListController = new QuestionListController();
		waitingQuestionListManager = new QuestionListManager();
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				waitingQuestionListController.getQuestionArrayList());
		adapter.setSortingOption(sortByDate);
		spinAdapter = new ArrayAdapter<String>(mcontext, R.layout.spinner_item,
				sortOption);
		mListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spinAdapter);
		sortOptionSpinner
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
						adapter.notifyDataSetChanged();
						onLoad();
					}
				}, 2000);
			}

			@Override
			public void onLoadMore() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						onLoad();
					}
				}, 2000);
			}
		});
	}

	private void onLoad() {
		timestamp = new Date();
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(timestamp.toString());
	}

	private class change_category_click implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			categoryID = position;
			// sort by Date
			if (categoryID == 0) {
				sortString = "date";
				adapter.setSortingOption(sortByDate);
			}
			// sort by Score
			if (categoryID == 1) {
				sortString = "score";
				adapter.setSortingOption(sortByScore);
			}
			// sort by Picture
			if (categoryID == 2) {
				sortString = "picture";
				adapter.setSortingOption(sortByPicture);
			}
			// sort by Question upvote
			if (categoryID == 3) {
				sortString = "q_upvote";
				adapter.setSortingOption(sortByQuestionUpvote);
			}
			// sort by Answer upvote
			if (categoryID == 4) {
				sortString = "a_upvote";
				adapter.setSortingOption(sortByAnswerUpvote);
			}
			updateSortedList();
		}

		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
	}

	private void updateList() {
		waitingListId = cacheController.getWaitingListId(mcontext);
		if (waitingListId.size() == 0)
			Toast.makeText(mcontext, "No Question are Waiting.",
					Toast.LENGTH_LONG).show();

		if (InternetConnectionChecker.isNetworkAvailable(this)) {
			Thread thread = new postListThread();
			thread.start();
		} else {
			waitingQuestionListController.clear();
			waitingQuestionList = cacheController
					.getWaitingQuestionList(mcontext);
			waitingQuestionListController.addAll(waitingQuestionList);
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
		}

	}

	private void updateSortedList() {
		runOnUiThread(doUpdateGUIList);
	}

	class postListThread extends Thread {
		
		@Override
		public void run() {
			waitingQuestionListController.clear();
			waitingQuestionList = cacheController
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
