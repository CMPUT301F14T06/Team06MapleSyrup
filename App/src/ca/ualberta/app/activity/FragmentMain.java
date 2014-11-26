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
import android.view.Window;
import android.view.WindowManager;
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
import ca.ualberta.app.widgets.CustomProgressDialog;

public class FragmentMain extends Fragment {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };
	private TextView titleBar;
	private EditText searchEditText;
	private CacheController cacheController;
	private QuestionListController questionListController;
	private QuestionListManager questionListManager;
	private QuestionListAdapter adapter = null;
	private Button searchButton;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	public String sortString = "date";
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
	private CustomProgressDialog progressDialog = null;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_main, container, false);
	}

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
	}

	@Override
	public void onStart() {
		super.onStart();
		questionListManager = new QuestionListManager();
		cacheController = new CacheController(mcontext);
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
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			searchButton.performClick();
			checkInternet();
		}
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

		mListView.setScrollListViewListener(new IXListViewListener() {

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

	private void onLoad() {
		timestamp = new Date();
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(timestamp.toString());
	}

	@Override
	public void onResume() {
		super.onResume();
		checkInternet();
	}

	private void checkInternet() {
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			titleBar.setText("Main");
			searchButton.setEnabled(true);
			searchEditText.setEnabled(true);
		} else {
			titleBar.setText("Main(Not Connected)");
			searchButton.setEnabled(false);
			searchEditText.setEnabled(false);
		}

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

	private void updateList() {
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			String searchString = searchEditText.getText().toString();
			Thread thread = new SearchThread(searchString);
			thread.start();
			mListView.setEnabled(true);
		}
	}

	private void updateSortedList() {
		getActivity().runOnUiThread(doUpdateGUIList);
	}

	class SearchThread extends Thread {
		private String search;

		public SearchThread(String s) {
			search = s;

		}

		@Override
		public void run() {
			cacheController.clear();
			Thread getMapThread = new GetMapThread();
			getMapThread.run();

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

		public DeleteThread(long questionID) {
			this.questionID = questionID;
		}

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