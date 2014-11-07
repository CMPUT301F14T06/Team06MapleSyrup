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
import ca.ualberta.app.view.ScrollListView;
import ca.ualberta.app.view.ScrollListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20

public class FragmentMain extends Fragment {
	static String sortByDate = "Sort By Date";
	static String sortByScore = "Sort By Score";
	static String sortByQuestionUpvote = "Sort By Question Upvote";
	static String sortByAnswerUpvote = "Sort By Answer Upvote";
	static String sortByPicture = "Sort By Picture";
	static String[] sortOption = { sortByDate, sortByScore, sortByPicture,
			sortByQuestionUpvote, sortByAnswerUpvote };

	private QuestionListAdapter adapter = null;
	private QuestionListController questionListController = null;
	private QuestionListController myQuestionListController = null;
	private CacheController cacheController;
	private TextView titleBar = null;
	private Spinner sortOptionSpinner;
	private QuestionListManager questionListManager;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	public String sortString = "Sort By Date";
	private String MYQUESTION;
	private QuestionList myQuestionList;
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	private long from = 0;
	private long size = 10;
	private long currentFrom = 0;
	private long TotalSize = 10;
	private int needToLoadMore = 0;

	// Thread to update adapter after an operation
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			if (needToLoadMore == 0) {
				adapter.applySortMethod();
			}
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mcontext = getActivity().getApplicationContext();
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Main");
		sortOptionSpinner = (Spinner) getView().findViewById(R.id.sort_spinner);

		mListView = (ScrollListView) getView()
				.findViewById(R.id.scrolllistView);
		mListView.setPullLoadEnable(true);
		mHandler = new Handler();

	}

	@Override
	public void onStart() {
		super.onStart();
		if (User.loginStatus == true) {
			// MYQUESTION = User.author.getUsername() + ".sav";
			myQuestionListController = new QuestionListController();
		}
		cacheController = new CacheController(mcontext);

		questionListManager = new QuestionListManager();
		questionListController = new QuestionListController();
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				questionListController.getQuestionArrayList());
		adapter.setSortingOption(sortByDate);
		spin_adapter = new ArrayAdapter<String>(mcontext,
				R.layout.spinner_item, sortOption);

		mListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		updateList();
		// Show details when click on a question
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionId = questionListController.getQuestion(pos)
						.getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionId);
				startActivity(intent);
			}

		});

		// Delete question on long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				Question question = questionListController
						.getQuestion(position);

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
		mListView.setScrollListViewListener(new IXListViewListener() {

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
						Toast.makeText(mcontext,
								"From: " + from + "  Size: " + size + "",
								Toast.LENGTH_LONG).show();
					}
				}, 2000);
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
						Toast.makeText(
								mcontext,
								"From: " + from + "  TotalSize: " + TotalSize
										+ "  ListSize: "
										+ questionListController.size() + "",
								Toast.LENGTH_LONG).show();
					}
				}, 2000);
			}
		});
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
			// updateList();
		}

		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
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
		adapter.notifyDataSetChanged();

	}

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
		// questionListController.clear();
		Thread searchThread = new SearchThread("");
		searchThread.start();
	}

	class SearchThread extends Thread {
		private String search;

		public SearchThread(String s) {
			search = s;

		}

		public void run() {
			if (needToLoadMore == 0) {
				questionListController.clear();
			}
			questionListController.addAll(questionListManager.searchQuestions(
					search, null, from, size));
			if (needToLoadMore == 0) {
				size = 10;
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
			tempFav = questionListManager.getQuestionMap(cacheController
					.getFavoriteId());
			tempSav = questionListManager.getQuestionMap(cacheController
					.getLocalCacheId());
			cacheController.addAll(mcontext, tempFav, tempSav);
		}
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
