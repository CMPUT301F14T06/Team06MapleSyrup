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
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.view.ScrollListView;
import ca.ualberta.app.view.ScrollListView.IXListViewListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the fragment activity for the search functionality, once a user
 * clicks the "Search" button on the bottom action bar
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni, Bicheng, Xiaocong
 */
public class FragmentMain extends Fragment {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };

	private String MYQUESTION;
	private TextView titleBar;
	private EditText searchEditText;
	private CacheController cacheController;
	private QuestionListController questionListController;
	private QuestionListController myQuestionListController;
	private QuestionListManager questionListManager;
	private QuestionList myQuestionList;
	private QuestionListAdapter adapter = null;
	private Button searchButton;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	public String sortString = "date";
	private int haveSearchResult = 0;
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	private long from = 0;
	private long size = 10;
	private long currentFrom = 0;
	private long TotalSize = 10;
	private int needToLoadMore = 0;

	/**
	 * Thread notify the adapter changes in data, and update the adapter after
	 * an operation
	 */
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			if (haveSearchResult == 0) {
				Toast.makeText(getActivity().getApplicationContext(),
						"No matched results", Toast.LENGTH_LONG).show();
			}
			if (needToLoadMore == 0) {
				adapter.applySortMethod();
			}
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	/**
	 * Once the fragment is active, the user interface, R.layout.fragment_search
	 * will be load into the fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	/**
	 * Once the fragment is created, this method will give each view an object
	 * to help other methods set data or listener
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mcontext = getActivity().getApplicationContext();
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Main");
		searchEditText = (EditText) getView().findViewById(
				R.id.question_EditText);
		searchButton = (Button) getView().findViewById(R.id.question_Button);
		sortOptionSpinner = (Spinner) getView().findViewById(R.id.sort_spinner);

		mListView = (ScrollListView) getView().findViewById(
				R.id.question_ListView);
		mListView.setPullLoadEnable(true);
		mHandler = new Handler();
	}

	/**
	 * onStart method Setup the adapter for the searching result list, and setup
	 * listener for each item (question) in the searching result list.
	 */
	@Override
	public void onStart() {
		super.onStart();
		questionListManager = new QuestionListManager();
		cacheController = new CacheController(mcontext);
		myQuestionListController = new QuestionListController();
		questionListController = new QuestionListController();
		myQuestionList = new QuestionList();
		adapter = new QuestionListAdapter(getActivity(),
				R.layout.single_question,
				questionListController.getQuestionArrayList());
		adapter.setSortingOption(sortByDate);

		spin_adapter = new ArrayAdapter<String>(mcontext,
				R.layout.spinner_item, sortOption);

		mListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		updateList();
		/**
		 * Setup the listener for the "Search" button is clicked, so that, once
		 * the button is clicked, the current result list will be updated to the
		 * newest searching result
		 */
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (searchButton.getText().equals("Search")
						&& !(searchEditText.getText().length() == 0)) {
					searchButton.setText("Cancel");
					searchEditText.clearFocus();
					updateList();
				} else {
					searchButton.setText("Search");
					searchEditText.clearFocus();
					searchEditText.setText("");
					updateList();
				}
			}
		});
		searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					searchButton.setText("Search");
				}
			}

		});
		searchEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchButton.setText("Search");
			}
		});

		/**
		 * Jump to the layout of the chosen question, and show details when
		 * click on an item (a question) in the searching result list
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionId = questionListController.getQuestion(pos - 1)
						.getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionId);
				startActivity(intent);
			}

		});

		/**
		 * Delete an item (a question) in the searching result list when a user
		 * long clicks the question.
		 */
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Question question = questionListController
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

		/**
		 * Update the current questions on screen, if a user scroll the
		 * searching result list
		 */
		mListView.setScrollListViewListener(new IXListViewListener() {

			/**
			 * Will called to update the content in the current result list when
			 * the data is changed or sorted; also, this method will tell the
			 * user the current interval of the question that are displayed on
			 * the screen
			 */
			@Override
			public void onRefresh() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						needToLoadMore = 0;
						from = 0;
						size = TotalSize;
						updateList();
						onLoad();
					}
				}, 2000);
			}

			/**
			 * this method will be called when a user up or down scroll the
			 * current result list to update the corresponding searching results
			 * on the screen; also, this method will tell the user the current
			 * interval of the question that are displayed on the screen
			 */
			@Override
			public void onLoadMore() {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						from = currentFrom;
						size = 10;
						if (from < questionListController.size()) {
							from += 10;
							TotalSize += 10;
						} else {
							from = questionListController.size();
							if (TotalSize - from > 10) {
								TotalSize -= 10;
							}
							Toast.makeText(mcontext, "All Questions loaded",
									Toast.LENGTH_LONG).show();
						}
						currentFrom = from;
						needToLoadMore = 1;
						updateList();
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

	@Override
	public void onResume() {
		super.onResume();
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			titleBar.setText("Main");
		} else {
			titleBar.setText("Main(Not Connected)");

		}
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
			//updateList();
		}

		/**
		 * Use default sort method is nothing is chosen
		 */
		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
	}

	/**
	 * Update the content of the searching result list by finding and loading
	 * the new list contents from the new searching result
	 */
	private void updateList() {
		if (User.loginStatus == true) {
			MYQUESTION = User.author.getUsername() + "my.sav";
			myQuestionListController.clear();
			Thread getListThread = new GetListThread();
			getListThread.start();
		}
		cacheController.clear();
		Thread getMapThread = new GetMapThread();
		getMapThread.start();
		String searchString = searchEditText.getText().toString();
		// searchEditText.setText("");
		Thread thread = new SearchThread(searchString);
		thread.start();
	}

	class GetListThread extends Thread {
		@Override
		public void run() {
			myQuestionListController.clear();
			myQuestionList = questionListManager.getQuestionList(User.author
					.getAuthorQuestionId());
			myQuestionListController.addAll(myQuestionList);
			QuestionListController.saveInFile(mcontext,
					myQuestionListController.getQuestionList(), MYQUESTION);
		}
	}

	/**
	 * this class will be called a thread of question list in the cache array
	 * for updating/other operations
	 */
	class GetMapThread extends Thread {
		@Override
		public void run() {
			cacheController.clear();
			Map<Long, Question> tempFav = new HashMap<Long, Question>();
			Map<Long, Question> tempSav = new HashMap<Long, Question>();
			tempFav = questionListManager.getQuestionMap(cacheController
					.getFavoriteId(mcontext));
			tempSav = questionListManager.getQuestionMap(cacheController
					.getLocalCacheId(mcontext));
			cacheController.addAll(mcontext, tempFav, tempSav);
		}
	}

	/**
	 * this class will be used to run thread for push and updating data
	 * 
	 * @author Anni, Bicheng
	 * 
	 */
	class SearchThread extends Thread {
		private String search;

		public SearchThread(String s) {
			search = s;

		}

		@Override
		public void run() {
			if (needToLoadMore == 0) {
				questionListController.clear();
			}
			questionListController.addAll(questionListManager.searchQuestions(
					search, null, from, size));
			if (needToLoadMore == 0) {
				size = 10;
			}

			if (questionListManager.searchQuestions(search, null, from, size)
					.size() != 0) {
				haveSearchResult = 1;
			} else {
				haveSearchResult = 0;
			}
			getActivity().runOnUiThread(doUpdateGUIList);
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
			questionListManager.deleteQuestion(questionID);

			// Remove movie from local list
			for (int i = 0; i < questionListController.size(); i++) {
				Question q = questionListController.getQuestion(i);

				if (q.getID() == questionID) {
					questionListController.removeQuestion(i);
					break;
				}
			}

			getActivity().runOnUiThread(doUpdateGUIList);
		}
	}
}