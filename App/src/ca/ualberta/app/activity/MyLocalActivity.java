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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.QuestionDetailActivity.SearchAuthorMapThread;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.view.ScrollListView;
import ca.ualberta.app.view.ScrollListView.IXListViewListener;
import ca.ualberta.app.widgets.CustomProgressDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the fragment activity for the functionality of displaying the user's
 * own question(s). It will be acted when a user clicks the "My Questions"
 * button in the user profile.
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni
 * @author Bicheng
 */
public class MyLocalActivity extends Activity {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };
	private QuestionListAdapter adapter;
	private QuestionListController localQuestionListController;
	private QuestionListManager localQuestionListManager;
	private QuestionList localQuestionList;
	private AuthorMapController authorMapController;
	private AuthorMapManager authorMapManager;
	private CacheController cacheController;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private ArrayList<Long> localListId;
	private static long categoryID;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	protected final String cacheList = "MYLOCAL";

	/**
	 * Thread notify the adapter changes in data, and update the adapter after
	 * an operation
	 */
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	/**
	 * onCreate method Once a user enter this activity, this method will give
	 * each view an object to help other methods set data or listeners.
	 * 
	 * @param savedInstanceState
	 *            The saved instance state bundle.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_local);
		sortOptionSpinner = (Spinner) findViewById(R.id.localSort_spinner);
		mListView = (ScrollListView) findViewById(R.id.localQuestion_ListView);
		mListView.setPullLoadEnable(false);
		mHandler = new Handler();
		mcontext = this;
	}

	/**
	 * onStart method Setup the adapter for the user's question list, and setup
	 * listener for each item (question) in the user's question list.
	 */
	@Override
	public void onStart() {
		super.onStart();
		cacheController = new CacheController(mcontext);
		authorMapController = new AuthorMapController(mcontext);
		authorMapManager = new AuthorMapManager();
		localQuestionListController = new QuestionListController();
		localQuestionListManager = new QuestionListManager();
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				localQuestionListController.getQuestionArrayList());
		adapter.setSortingOption(sortByDate);
		spin_adapter = new ArrayAdapter<String>(mcontext,
				R.layout.spinner_item, sortOption);
		mListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		updateList();

		/**
		 * Jump to the layout of the choosen question, and show details when
		 * click on an item (a question) in the favorite question list
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * display the layout of the chosen question, and show details when
			 * click on an item (a question) in the searching result list
			 * 
			 * @param parent
			 *            The adapter of the item in the list.
			 * @param view
			 *            The view.
			 * @param pos
			 *            The position of a question.
			 * @param id
			 *            The ID of a question.
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {

				Question question = localQuestionListController
						.getQuestion(pos - 1);
				long questionID = question.getID();
				String questionTitle = localQuestionListController.getQuestion(
						pos - 1).getTitle();
				if (!cacheController.hasSaved(mcontext, question)) {
					cacheController.addLocalQuestion(mcontext, question);
				}
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);
				intent.putExtra(QuestionDetailActivity.QUESTION_TITLE,
						questionTitle);
				intent.putExtra(QuestionDetailActivity.CACHE_LIST, cacheList);
				startActivity(intent);
			}
		});

		/**
		 * Delete an item (a question) in the favorite list when a user long
		 * clicks the question.
		 */
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			/**
			 * If the user is the author of the question, and the user long
			 * click the item (the question) in the question list, then remove
			 * the selected question from the question list.
			 * 
			 * @param parent
			 *            The adapter of the item in the list.
			 * @param view
			 *            The view.
			 * @param pos
			 *            The position of a question.
			 * @param id
			 *            The ID of a question.
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Question question = localQuestionListController
						.getQuestion(position - 1);
				if (User.author != null
						&& User.author.getUserId() == question.getUserId()) {
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

		/**
		 * Update the current questions on screen, if a user scroll his/her
		 * favorite question list
		 */
		mListView.setScrollListViewListener(new IXListViewListener() {
			/**
			 * Will called to update the content in the favorite question list
			 * when the data is changed or sorted; also, this method will tell
			 * the user the current interval of the question that are displayed
			 * on the screen
			 */
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

			/**
			 * this method will be called when a user up or down scroll the
			 * favorite question list to update the corresponding questions on
			 * the screen; also, this method will tell the user the current
			 * interval of the question that are displayed on the screen
			 */
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

	/**
	 * stop refresh and loading, reset header and the footer view.
	 */
	private void onLoad() {
		timestamp = new Date();
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(timestamp.toString());
	}

	/**
	 * This class represents the functions in the sorting menu in the spinner at
	 * the top of the screen
	 */
	private class change_category_click implements OnItemSelectedListener {
		/**
		 * Based on different conditions, call different sorting functions.
		 * 
		 * @param parent
		 *            The adapter of the item in the list.
		 * @param view
		 *            The view.
		 * @param pos
		 *            The position of a question.
		 * @param id
		 *            The ID of a question.
		 */
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

		/**
		 * Use default sort method is nothing is chosen
		 */
		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
	}

	/**
	 * Update the content of the main question list by finding and loading the
	 * new list contents from the data set (local/online server)
	 */
	private void updateList() {
		localListId = cacheController.getLocalCacheId(mcontext);
		if (localListId.size() == 0)
			Toast.makeText(mcontext, "No Question Cached Yet.",
					Toast.LENGTH_LONG).show();

		if (InternetConnectionChecker.isNetworkAvailable()) {
			Thread thread = new GetMapThread();
			thread.start();

		}
		localQuestionListController.clear();
		localQuestionList = cacheController.getLocalQuestionsList(mcontext);
		localQuestionListController.addAll(localQuestionList);
		updateSortedList();

	}

	private void updateSortedList() {
		runOnUiThread(doUpdateGUIList);
	}

	class GetMapThread extends Thread {

		@Override
		public void run() {
			Thread searchAuthorThread = new SearchAuthorMapThread("");
			searchAuthorThread.run();
			cacheController.clear();
			Map<Long, Question> tempFav = new HashMap<Long, Question>();
			Map<Long, Question> tempSav = new HashMap<Long, Question>();
			Map<Long, Question> tempMyQuest = new HashMap<Long, Question>();
			tempFav = localQuestionListManager.getQuestionMap(cacheController
					.getFavoriteId(mcontext));
			tempSav = localQuestionListManager.getQuestionMap(cacheController
					.getLocalCacheId(mcontext));
			if (User.loginStatus)
				tempMyQuest = localQuestionListManager
						.getQuestionMap(User.author.getAuthorQuestionId());
			cacheController.addAll(mcontext, tempFav, tempSav, tempMyQuest);
		}
	}

	class DeleteThread extends Thread {
		private long questionID;

		public DeleteThread(long questionID) {
			this.questionID = questionID;
		}

		@Override
		public void run() {
			localQuestionListManager.deleteQuestion(questionID);
			// Remove question from local list
			for (int i = 0; i < localQuestionListController.size(); i++) {
				Question q = localQuestionListController.getQuestion(i);
				if (q.getID() == questionID) {
					localQuestionListController.removeQuestion(i);
					break;
				}
			}
			runOnUiThread(doUpdateGUIList);
		}
	}

	class SearchAuthorMapThread extends Thread {
		private String search;

		public SearchAuthorMapThread(String s) {
			search = s;
		}

		@Override
		public void run() {
			authorMapController.clear();
			authorMapController.putAll(authorMapManager.searchAuthors(search,
					null, 0, 1000, "author"));
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
