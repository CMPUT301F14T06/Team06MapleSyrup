package ca.ualberta.app.activity;

import java.util.ArrayList;
import java.util.Date;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the fragment activity for the users' favorite question list, once a user clicks the "My Favorite" button on the bottom action bar
 * 
 * The fragment part is from this web site: http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni
 */
public class FragmentFavorite extends Fragment {
	private TextView titleBar;
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
	 * Thread notify the adapter changes in data, and update the adapter after an operation
	 */
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	/**
	 * Once the fragment is active, the user interface, R.layout.fragment_fav will be load into the fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_fav, container, false);
	}

	/**
	 * Once the fragment is created, this method will give each view an object to help other methods set data or listener
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Favorite");
		mcontext = getActivity().getApplicationContext();
		// favQuestionListView = (ListView) getActivity().findViewById(
		// R.id.favQuestion_ListView);
		sortOptionSpinner = (Spinner) getActivity().findViewById(
				R.id.favSort_spinner);
		mListView = (ScrollListView) getView().findViewById(
				R.id.favQuestion_ListView);
		mListView.setPullLoadEnable(true);
		mHandler = new Handler();

	}

	/**
	 * onStart method
	 * Setup the adapter for the users' favorite question list, and setup listener for each item (question) in the favorite list.
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
		 * Jump to the layout of the choosen question, and show details when click on an item (a question) in the favorite question list
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionID = favQuestionListController.getQuestion(pos)
						.getID();
				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);
				startActivity(intent);
			}
		});
		
		/**
		 *  Delete an item (a question) in the favorite list when a user long clicks the question.
 		 */
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Question question = favQuestionListController
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
		/**
		 * Update the current questions on screen, if a user scroll his/her favorite question list
		 */
		mListView.setScrollListViewListener(new IXListViewListener() {

			/**
			 * Will called to update the content in the favorite question list when the data is changed or sorted;
			 * also, this method will tell the user the current interval of the question that are displayed on the screen
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
			 * this method will be called when a user up or down scroll the favorite question list to update the corresponding questions on the screen;
			 * also, this method will tell the user the current interval of the question that are displayed on the screen
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
	 * onResume method
	 */
	@Override
	public void onResume() {
		super.onResume();
		// updateList();
	}

	/**
	 * This class represents the functions in the sorting menu in the spinner at the top of the screen
	 * @author Anni
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
	 * Update the content of the main question list by finding and loading the new list contents from the data set (local/online server)
	 */
	private void updateList() {
		favListId = cacheController.getFavoriteId();
		if (favListId.size() == 0)
			Toast.makeText(mcontext, "No Favorite Question Added Yet.",
					Toast.LENGTH_LONG).show();
		Thread thread = new GetListThread();
		thread.start();
	}

	/**
	 * this class will be called a thread of question list in the cache array for updating/other operations
	 */
	class GetListThread extends Thread {
		@Override
		public void run() {
			favQuestionListController.clear();
			favQuestionList = favQuestionListManager.getQuestionList(favListId);
			favQuestionListController.addAll(favQuestionList);

			getActivity().runOnUiThread(doUpdateGUIList);
		}
	}

	class DeleteThread extends Thread {
		private long questionID;

		/**
		 * delete a thread
		 * @param questionID the ID for the thread of a question
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
			getActivity().runOnUiThread(doUpdateGUIList);
		}
	}
}