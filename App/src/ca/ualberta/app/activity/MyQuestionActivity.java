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

import java.util.Date;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.QuestionListAdapter;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the activity of the user's personal question list. It is similar to
 * the main question list, except that only questions that asked by the current
 * user will be shown
 * 
 * @author Anni, Bicheng, Xiaocong
 * 
 */
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
	private QuestionList myQuestionList;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	private String MYQUESTION;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;

	/**
	 * Thread to update adapter after an operation
	 */
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	/**
	 * onCreate method. Once the activity is created, first set the content
	 * view, and initialize the list view of "my question", and a Spinner for
	 * sort options.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_question);
		mcontext = this;
		sortOptionSpinner = (Spinner) findViewById(R.id.my_question_sort_spinner);
		mListView = (ScrollListView) findViewById(R.id.my_question_ListView);
		mListView.setPullLoadEnable(false);
		mHandler = new Handler();
	}

	@Override
	public void onStart() {
		super.onStart();
		MYQUESTION = User.author.getUsername() + "my.sav";
		myQuestionListController = new QuestionListController();
		myQuestionListManager = new QuestionListManager();
		adapter = new QuestionListAdapter(this, R.layout.single_question,
				myQuestionListController.getQuestionArrayList());
		adapter.setSortingOption(sortByDate);
		spin_adapter = new ArrayAdapter<String>(mcontext,
				R.layout.spinner_item, sortOption);
		mListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		updateList();
		/**
		 * Show details when click on a question
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionID = myQuestionListController.getQuestion(pos - 1)
						.getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);
				startActivity(intent);
			}
		});
		/**
		 * Delete question on long click
		 */
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Question question = myQuestionListController
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
			/**
			 * Will called to update the content in the user's own question list
			 * when the data is changed or sorted;
			 */
			@Override
			public void onRefresh() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						updateList();
						onLoad();
					}
				}, 2000);
			}

			/**
			 * this method will be called when a user up or down scroll the
			 * user's own question list;
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
	 * This class represents the functions in the sorting menu
	 */
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
			// updateList();
		}

		/**
		 * Use default sort method is nothing is chosen
		 */
		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
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
	 * Update the content of the user's own question list by finding and loading
	 * the new list contents from the data set (local/online server)
	 */
	private void updateList() {
		if (InternetConnectionChecker.isNetworkAvailable(this)) {
			QuestionListController.saveInFile(mcontext,
					myQuestionListController.getQuestionList(), MYQUESTION);
			Thread thread = new GetListThread();
			thread.start();
		} else {
		}
	}

	/**
	 * get a thread of the corresponding question list
	 */
	class GetListThread extends Thread {
		@Override
		public void run() {
			myQuestionListController.clear();
			myQuestionList = myQuestionListManager.getQuestionList(User.author
					.getAuthorQuestionId());
			myQuestionListController.addAll(myQuestionList);

			runOnUiThread(doUpdateGUIList);
		}
	}

	class DeleteThread extends Thread {
		private long questionID;

		/**
		 * delete a thread
		 * 
		 * @param questionID
		 *            the ID for the thread of a question
		 */
		public DeleteThread(long questionID) {
			this.questionID = questionID;
		}

		/**
		 * We need to remove the question from the list as well
		 */
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
}