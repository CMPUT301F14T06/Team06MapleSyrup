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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.gps.Location;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.network.NetworkObserver;
import ca.ualberta.app.view.ScrollListView;
import ca.ualberta.app.view.ScrollListView.IXListViewListener;
import ca.ualberta.app.widgets.CustomProgressDialog;

/**
 * This is the fragment activity for the search functionality, once a user
 * clicks the "Search" button on the bottom action bar
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni
 * @author Bicheng
 */
public class FragmentMain extends Fragment {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String sortByDistance = "Sort By Distance";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote, sortByDistance };
	private Spinner sortOptionSpinner;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	public String sortString = "date";
	private TextView titleBar;
	private EditText searchEditText;
	private CacheController cacheController;
	private QuestionListController questionListController;
	private QuestionListManager questionListManager;
	private QuestionListAdapter adapter = null;
	private AuthorMapController authorMapController;
	private AuthorMapManager authorMapManager;
	private Button searchButton;
	private Context mcontext;
	private int haveSearchResult = 1;
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	private long from = 0;
	private long size = 10;
	private long currentFrom = 0;
	private long TotalSize = 10;
	private int needToLoadMore = 0;
	private InputMethodManager imm;
	private NetworkObserver networkObserver;
	private CustomProgressDialog progressDialog = null;

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
			stopProgressDialog();
		}

	};

	/**
	 * Once the fragment is active, the user interface, R.layout.fragment_search
	 * will be load into the fragment.
	 * 
	 * @param inflater
	 *            is used to find out the layout defined in the xml file.
	 * @param container
	 *            the view container that contains all views of an single item.
	 * @param savedInstanceState
	 *            the saved instance state bundle.
	 * 
	 * @return inflater the layout of this fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	/**
	 * Once the fragment is created, this method will give each view an object
	 * to help other methods set data or listener.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state bundle.
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
		imm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		mListView = (ScrollListView) getView().findViewById(
				R.id.question_ListView);
		mListView.setPullLoadEnable(true);
		mHandler = new Handler();
		networkObserver = new NetworkObserver();
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
		authorMapController = new AuthorMapController(mcontext);
		authorMapManager = new AuthorMapManager();
		questionListController = new QuestionListController();
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
		searchButton.setOnClickListener(new OnClickListener() {

			/**
			 * Setup the listener for the "Search" button, so that, once the
			 * button is clicked, the current result list will be updated to the
			 * newest searching result
			 * 
			 * @param v
			 *            The view of the button.
			 */
			@Override
			public void onClick(View v) {
				if (searchButton.getText().equals("Search")
						&& !(searchEditText.getText().length() == 0)) {
					startProgressDialog();
					searchButton.setText("Cancel");
					searchEditText.clearFocus();
					imm.hideSoftInputFromWindow(
							searchEditText.getWindowToken(), 0);
					updateList();
				} else {
					startProgressDialog();
					searchButton.setText("Search");
					searchEditText.setText("");
					searchEditText.clearFocus();
					imm.hideSoftInputFromWindow(
							searchEditText.getWindowToken(), 0);
					updateList();
				}
			}
		});

		searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			/**
			 * Setup the listener for the Search edit text, so that, once the
			 * edit text bar is filled, the app can get the user input text.
			 * 
			 * @param v
			 *            The view of the button.
			 * @param hasFocus
			 *            True if the edit text bar are edited.
			 */
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
				long questionId = questionListController.getQuestion(pos - 1)
						.getID();
				String questionTitle = questionListController.getQuestion(
						pos - 1).getTitle();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionId);
				intent.putExtra(QuestionDetailActivity.QUESTION_TITLE,
						questionTitle);
				startActivity(intent);
			}

		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			/**
			 * Delete an item (a question) in the searching result list when a
			 * user long clicks the question.
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

				Question question = questionListController
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
		 * Update the current questions on screen, if a user scroll the
		 * searching result list
		 */
		mListView.setScrollListViewListener(new IXListViewListener() {
			/**
			 * This method will called to update the content in the current
			 * result list when the data is changed or sorted; also, this method
			 * will tell the user the current interval of the question that are
			 * displayed on the screen
			 */
			@Override
			public void onRefresh() {
				mListView.setEnabled(false);
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
				// mListView.setEnabled(true);
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
		if (InternetConnectionChecker.isNetworkAvailable()) {
			searchButton.performClick();
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
	 * onResume method Once this fragment resumes from other operations, set the
	 * text in the title bar.
	 */
	@Override
	public void onResume() {
		super.onResume();
		checkInternet();
	}

	/**
	 * disable search functionality when Internet is not available
	 */
	private void checkInternet() {
		if (InternetConnectionChecker.isNetworkAvailable()) {
			titleBar.setText("Main");
			searchButton.setEnabled(true);
			searchEditText.setEnabled(true);
		} else {
			titleBar.setText("Main(Not Connected)");
			searchButton.setEnabled(false);
			searchEditText.setEnabled(false);
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

			// sort by distance
			if (categoryID == 5) {
				Location.getLocationCoordinates();
				adapter.setSortingOption(sortByDistance);
			}
			updateSortedList();
		}

		/**
		 * Use default sort method is nothing is chosen
		 * 
		 * @param parent
		 *            The adapter of the item in the list.
		 */
		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
	}

	/**
	 * Update the content of the searching result list by finding and loading
	 * the new list contents from the new searching result
	 */
	public void updateList() {
		if (InternetConnectionChecker.isNetworkAvailable()) {
			String searchString = searchEditText.getText().toString();
			Thread thread = new SearchThread(searchString);
			thread.start();
			mListView.setEnabled(true);
			networkObserver.setObserver(this);
		} else {
			networkObserver.startObservation(this);
		}
	}

	
	/**
	 * apply the sort method to current listView and update
	 */
	private void updateSortedList() {
		adapter.applySortMethod();
		adapter.notifyDataSetChanged();
	}

	/**
	 * this class will be used to run thread to load data when the question list
	 * is searched.
	 */
	class SearchThread extends Thread {
		private String search;

		/**
		 * the constructor of the class
		 * 
		 * @param s
		 *            the keyword.
		 */
		public SearchThread(String s) {
			search = s;

		}

		/**
		 * check if there are search result
		 */
		@Override
		public void run() {
			cacheController.clear();
			Thread getMapThread = new GetMapThread();
			getMapThread.run();
			authorMapController.clear();
			Thread searchAuthorThread = new SearchAuthorMapThread("");
			searchAuthorThread.run();

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

	class GetMapThread extends Thread {

		@Override
		public void run() {

			cacheController.clear();
			Map<Long, Question> tempFav = new HashMap<Long, Question>();
			Map<Long, Question> tempSav = new HashMap<Long, Question>();
			Map<Long, Question> tempMyQuest = new HashMap<Long, Question>();
			tempFav = questionListManager.getQuestionMap(cacheController
					.getFavoriteId(mcontext));
			tempSav = questionListManager.getQuestionMap(cacheController
					.getLocalCacheId(mcontext));

			if (User.loginStatus)
				tempMyQuest = questionListManager.getQuestionMap(User.author
						.getAuthorQuestionId());

			cacheController.addAll(mcontext, tempFav, tempSav, tempMyQuest);
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
		 * to remove the question from the list
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

	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(getActivity());

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