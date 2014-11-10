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
 * favorite question(s). It will be acted when a user clicks the "Favorite Questions"
 * button in the user profile.
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni
 * @author Bicheng
 * @author Xiaocong
 */
public class MyFavoriteActivity extends Activity {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };
	private QuestionListAdapter adapter = null;
	private QuestionListController favQuestionListController;
	private QuestionListManager favQuestionListManager;
	private QuestionList favQuestionList;
	private CacheController cacheController;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private ArrayList<Long> favListId;
	private static long categoryID;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;

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
	 * onCreate method
	 * Once a user enter this activity, this method will give each view an object
	 * to help other methods set data or listeners.
	 * 
	 * @param savedInstanceState The saved instance state bundle.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_favorite);
		// favQuestionListView = (ListView) getActivity().findViewById(
		// R.id.favQuestion_ListView);
		sortOptionSpinner = (Spinner) findViewById(R.id.favSort_spinner);
		mListView = (ScrollListView) findViewById(R.id.favQuestion_ListView);
		mListView.setPullLoadEnable(false);
		mHandler = new Handler();
		mcontext = this;
	}

	/**
	 * onStart method
	 * Setup the adapter for the users' favorite question list,
	 * and setup listener for each item (question) in the favorite list.
	 */
	@Override
	public void onStart() {
		super.onStart();
		// FAVQUESTION = User.author.getUsername() + "FAV.sav";
		cacheController = new CacheController(mcontext);
		favQuestionListController = new QuestionListController();
		favQuestionListManager = new QuestionListManager();
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				favQuestionListController.getQuestionArrayList());
		adapter.setSortingOption(sortByDate);
		spin_adapter = new ArrayAdapter<String>(mcontext,
				R.layout.spinner_item, sortOption);
		mListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		updateList();
		
		/**
		 * act the layout of the chosen question, and show details when
		 * click on an item (a question) in the favorite question list
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * display the layout of the chosen question, and show details when
			 * click on an item (a question) in the searching result list
			 * 
			 * @param parent The adapter of the item in the list.
			 * @param view The view.
			 * @param pos The position of a question.
			 * @param id The ID of a question. 
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionID = favQuestionListController
						.getQuestion(pos - 1).getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);
				startActivity(intent);
			}
		});

		/**
		 * Delete an item (a question) in the favorite list when a user long
		 * clicks the question.
		 */
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			/**
			 * If the user is the author of the question, and the user long click the
			 * item (the question) in the question list, then remove the selected question
			 * from the question list.
			 * 
			 * @param parent The adapter of the item in the list.
			 * @param view The view.
			 * @param pos The position of a question.
			 * @param id The ID of a question. 
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Question question = favQuestionListController
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

		// updateList();
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
						updateList();
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

//	/**
//	 * onResume method
//	 */
//	@Override
//	public void onResume() {
//		super.onResume();
//		updateList();
//	}

	/**
	 * This class represents the functions in the sorting menu in the spinner at
	 * the top of the screen
	 */
	private class change_category_click implements OnItemSelectedListener {
		/**
		 * Based on different conditions, call different sorting functions.
		 * 
		 * @param parent The adapter of the item in the list.
		 * @param view The view.
		 * @param pos The position of a question.
		 * @param id The ID of a question. 
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
	 * Update the content of the main question list by finding and loading the
	 * new list contents from the data set (local/online server)
	 */
	private void updateList() {
		favListId = cacheController.getFavoriteId(mcontext);
		if (favListId.size() == 0)
			Toast.makeText(mcontext, "No Favorite Question Added Yet.",
					Toast.LENGTH_LONG).show();

		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			Thread thread = new GetListThread();
			thread.start();
			
		} else {
			favQuestionListController.clear();
			favQuestionList = cacheController.getFavoriteQuestionList();
			favQuestionListController.addAll(favQuestionList);
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();	
		}

	}

	/**
	 * this class will be called a thread of question list in the cache array
	 * to load the question list.
	 */
	class GetListThread extends Thread {
		
		/**
		 * load questions by question ID
		 */
		@Override
		public void run() {
			favQuestionListController.clear();
			favQuestionList = favQuestionListManager.getQuestionList(favListId);
			favQuestionListController.addAll(favQuestionList);

			runOnUiThread(doUpdateGUIList);
		}
	}

	class DeleteThread extends Thread {
		private long questionID;

		/**
		 * delete a thread
		 * 
		 * @param questionID the ID for the thread of a question.
		 */
		public DeleteThread(long questionID) {
			this.questionID = questionID;
		}

		/**
		 * We need to remove the question from the list as well
		 */
		@Override
		public void run() {
			favQuestionListManager.deleteQuestion(questionID);
			// Remove movie from local list
			for (int i = 0; i < favQuestionListController.size(); i++) {
				Question q = favQuestionListController.getQuestion(i);
				if (q.getID() == questionID) {
					favQuestionListController.removeQuestion(i);
					break;
				}
			}
			runOnUiThread(doUpdateGUIList);
		}
	}

}
