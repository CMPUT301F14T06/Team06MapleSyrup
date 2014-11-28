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

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.view.ScrollListView;
import ca.ualberta.app.view.ScrollListView.IXListViewListener;
import ca.ualberta.app.widgets.CustomProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyQuestionActivity extends Activity {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };
	private QuestionListAdapter adapter = null;
	private QuestionListController myQuestionListController;
	private QuestionListManager myQuestionListManager;
	private AuthorMapController authorMapController;
	private CacheController cacheController;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spinAdapter;
	private ArrayList<Long> myQuestionId;
	private static long categoryID;
	private CustomProgressDialog progressDialog = null;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	protected final String cacheList = "MYQUESTION";
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
			spinAdapter.notifyDataSetChanged();
			stopProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_question);
		sortOptionSpinner = (Spinner) findViewById(R.id.my_question_sort_spinner);
		mListView = (ScrollListView) findViewById(R.id.my_question_ListView);
		mListView.setPullLoadEnable(false);
		mHandler = new Handler();
		mcontext = this;
	}

	@Override
	public void onStart() {
		super.onStart();
		cacheController = new CacheController(mcontext);
		authorMapController = new AuthorMapController(mcontext);
		myQuestionListController = new QuestionListController();
		myQuestionListManager = new QuestionListManager();
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				myQuestionListController.getQuestionArrayList());
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
				long questionID = myQuestionListController.getQuestion(pos - 1)
						.getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);
				intent.putExtra(QuestionDetailActivity.CACHE_LIST, cacheList);
				startActivity(intent);
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Question question = myQuestionListController
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
		startProgressDialog();
		myQuestionId = User.author.getAuthorQuestionId();
		if (myQuestionId.size() == 0)
			Toast.makeText(mcontext, "No Question Asked Yet.",
					Toast.LENGTH_LONG).show();

		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			Thread thread = new GetMapThread();
			thread.start();

		}
		myQuestionListController.clear();
		myQuestionListController.addAll(cacheController
				.getMyQuestionList(mcontext));
		runOnUiThread(doUpdateGUIList);
	}

	private void updateSortedList() {
		runOnUiThread(doUpdateGUIList);
	}

	class GetMapThread extends Thread {

		@Override
		public void run() {
			authorMapController.renewAuthorMap(mcontext);
			cacheController.clear();
			Map<Long, Question> tempFav = new HashMap<Long, Question>();
			Map<Long, Question> tempSav = new HashMap<Long, Question>();
			Map<Long, Question> tempMyQuest = new HashMap<Long, Question>();
			tempFav = myQuestionListManager.getQuestionMap(cacheController
					.getFavoriteId(mcontext));
			tempSav = myQuestionListManager.getQuestionMap(cacheController
					.getLocalCacheId(mcontext));
			tempMyQuest = myQuestionListManager.getQuestionMap(User.author
					.getAuthorQuestionId());
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
			myQuestionListManager.deleteQuestion(questionID);
			// Remove movie from local list
			for (int i = 0; i < myQuestionListController.size(); i++) {
				Question q = myQuestionListController.getQuestion(i);
				if (q.getID() == questionID) {
					myQuestionListController.removeQuestion(i);
					break;
				}
			}
			runOnUiThread(doUpdateGUIList);
		}
	}

	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);

			progressDialog.setMessage("Loading...");
		}
		progressDialog.show();
	}

	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
