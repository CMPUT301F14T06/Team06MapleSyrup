package ca.ualberta.app.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.adapter.AnswerWaitingListAdapter;
import ca.ualberta.app.adapter.QuestionWaitingListAdapter;
import ca.ualberta.app.adapter.ReplyWaitingListAdapter;
import ca.ualberta.app.controller.AnswerListController;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.PushController;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.controller.ReplyListController;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.network.NetworkObserver;
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

/**
 * This is the activity of the waiting list of un-posted question(s), answer(s)
 * and reply(s). They will be post once the Internet is connected.
 * 
 * @author Bicheng
 * 
 */
public class WaitingListActivity extends Activity {

	private QuestionWaitingListAdapter questionAdapter;
	private AnswerWaitingListAdapter answerAdapter;
	private ReplyWaitingListAdapter replyAdapter;
	private QuestionListController waitingQuestionListController;
	private AnswerListController waitingAnswerListController;
	private ReplyListController waitingReplyListController;
	private QuestionListManager waitingQuestionListManager;
	private QuestionList waitingQuestionList;
	private AuthorMapController authorMapController;
	private AuthorMapManager authorMapManager;
	private PushController pushController;
	private Spinner typeOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spinAdapter;
	private static long categoryID;
	public String sortString = "Sort By Date";
	private Date timestamp;
	private ScrollListView mListView;
	private Handler mHandler;
	private String QuestionType;
	private String AnswerType;
	private String ReplyType;
	private ArrayList<String> typeOption;
	private NetworkObserver networkObserver;

	/**
	 * Thread notify the adapter changes in data, and update the adapter after
	 * an operation
	 */
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			questionAdapter.notifyDataSetChanged();
			answerAdapter.notifyDataSetChanged();
			replyAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.activity_waiting_list);
		typeOptionSpinner = (Spinner) findViewById(R.id.waiting_List_type_spinner);
		mListView = (ScrollListView) findViewById(R.id.waiting_List_ListView);
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
		typeOption = new ArrayList<String>();
		networkObserver = new NetworkObserver();
		pushController = new PushController(mcontext);
		authorMapController = new AuthorMapController(mcontext);
		waitingQuestionListController = new QuestionListController();
		waitingAnswerListController = new AnswerListController();
		waitingReplyListController = new ReplyListController();
		waitingQuestionListManager = new QuestionListManager();
		authorMapManager = new AuthorMapManager();
		questionAdapter = new QuestionWaitingListAdapter(mcontext,
				R.layout.single_waiting_question,
				waitingQuestionListController.getQuestionArrayList());
		answerAdapter = new AnswerWaitingListAdapter(mcontext,
				R.layout.single_waiting_answer,
				waitingAnswerListController.getAnswerList());
		replyAdapter = new ReplyWaitingListAdapter(mcontext,
				R.layout.single_waiting_reply,
				waitingReplyListController.getReplyList());

		mListView.setAdapter(questionAdapter);

		updateCounter();
		spinAdapter = new ArrayAdapter<String>(mcontext, R.layout.spinner_item,
				typeOption);

		typeOptionSpinner.setAdapter(spinAdapter);
		typeOptionSpinner
				.setOnItemSelectedListener(new change_category_click());

		mListView.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * allow user to edit these un-posted question, answer or reply
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
				// Question
				if (categoryID == 0) {
					Intent intent = new Intent(mcontext,
							CreateQuestionActivity.class);
					intent.putExtra(CreateQuestionActivity.QUESTION_ID,
							waitingQuestionListController.getQuestion(pos - 1)
									.getID());
					intent.putExtra(CreateQuestionActivity.QUESTION_TITLE,
							waitingQuestionListController.getQuestion(pos - 1)
									.getTitle());
					intent.putExtra(CreateQuestionActivity.QUESTION_CONTENT,
							waitingQuestionListController.getQuestion(pos - 1)
									.getContent());
					try {
						intent.putExtra(
								CreateQuestionActivity.IMAGE,
								waitingQuestionListController.getQuestion(
										pos - 1).getImage());
					} catch (Exception e) {
					}

					startActivity(intent);
					// Answer
				} else if (categoryID == 1) {
					Intent intent = new Intent(mcontext,
							CreateAnswerActivity.class);
					intent.putExtra(CreateAnswerActivity.QUESTION_ID,
							waitingAnswerListController.getAnswer(pos - 1)
									.getQuestionID());
					intent.putExtra(CreateAnswerActivity.QUESTION_TITLE,
							waitingAnswerListController.getAnswer(pos - 1)
									.getQuestionTitle());
					intent.putExtra(CreateAnswerActivity.ANSWER_ID,
							waitingAnswerListController.getAnswer(pos - 1)
									.getID());
					intent.putExtra(CreateAnswerActivity.ANSWER_CONTENT,
							waitingAnswerListController.getAnswer(pos - 1)
									.getContent());
					try {
						intent.putExtra(CreateAnswerActivity.IMAGE,
								waitingAnswerListController.getAnswer(pos - 1)
										.getImage());
					} catch (Exception e) {
					}

					intent.putExtra(CreateAnswerActivity.EDIT_MODE, true);
					startActivity(intent);
					// Reply
				} else if (categoryID == 2) {
					if (waitingReplyListController.getReply(pos - 1)
							.getAnswerID() == 0) {
						Intent intent = new Intent(mcontext,
								CreateQuestionReplyActivity.class);
						intent.putExtra(
								CreateQuestionReplyActivity.QUESTION_ID,
								waitingReplyListController.getReply(pos - 1)
										.getQuestionID());
						intent.putExtra(
								CreateQuestionReplyActivity.QUESTION_TITLE,
								waitingReplyListController.getReply(pos - 1)
										.getQuestionTitle());
						intent.putExtra(CreateQuestionReplyActivity.REPLY_ID,
								waitingReplyListController.getReply(pos - 1)
										.getID());
						intent.putExtra(
								CreateQuestionReplyActivity.REPLY_CONTENT,
								waitingReplyListController.getReply(pos - 1)
										.getContent());
						intent.putExtra(CreateAnswerActivity.EDIT_MODE, true);
						startActivity(intent);
					} else {
						Intent intent = new Intent(mcontext,
								CreateAnswerReplyActivity.class);
						intent.putExtra(CreateAnswerReplyActivity.QUESTION_ID,
								waitingReplyListController.getReply(pos - 1)
										.getQuestionID());
						intent.putExtra(
								CreateAnswerReplyActivity.QUESTION_TITLE,
								waitingReplyListController.getReply(pos - 1)
										.getQuestionTitle());
						intent.putExtra(CreateAnswerReplyActivity.ANSWER_ID,
								waitingReplyListController.getReply(pos - 1)
										.getAnswerID());
						intent.putExtra(CreateAnswerReplyActivity.REPLY_ID,
								waitingReplyListController.getReply(pos - 1)
										.getID());
						intent.putExtra(
								CreateAnswerReplyActivity.REPLY_CONTENT,
								waitingReplyListController.getReply(pos - 1)
										.getContent());
						intent.putExtra(CreateAnswerReplyActivity.EDIT_MODE,
								true);
						startActivity(intent);
					}
				}
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
				Question question = waitingQuestionListController
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
						questionAdapter.notifyDataSetChanged();
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
						questionAdapter.notifyDataSetChanged();
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
	 * will update the size of each listView
	 */
	private void updateCounter() {
		typeOption.clear();
		QuestionType = "Questions (" + waitingQuestionListController.size()
				+ ")";
		AnswerType = "Answers (" + waitingAnswerListController.size() + ")";
		ReplyType = "Replies (" + waitingReplyListController.size() + ")";
		typeOption.add(QuestionType);
		typeOption.add(AnswerType);
		typeOption.add(ReplyType);
	}

	/**
	 * Update the content of listView whicha re the question,answer and reply
	 */
	public void updateList() {
		if (InternetConnectionChecker.isNetworkAvailable() && User.loginStatus) {
			long total = waitingQuestionListController.size()
					+ waitingAnswerListController.size()
					+ waitingReplyListController.size();
			Toast.makeText(mcontext,
					total + " item(s) posted from Waiting List",
					Toast.LENGTH_LONG).show();
			Thread updateAuthor = new UpdateAuthorThread();
			updateAuthor.start();
			Thread thread = new PostListThread();
			thread.start();
			networkObserver.setObserver(this);

		} else {
			waitingQuestionListController.clear();
			waitingAnswerListController.clear();
			waitingReplyListController.clear();

			waitingQuestionList = pushController
					.getWaitingQuestionList(mcontext);
			waitingQuestionListController.addAll(waitingQuestionList);
			waitingAnswerListController.addAll(pushController
					.getWaitingAnswerList(mcontext));
			waitingReplyListController.addAll(pushController
					.getWaitingReplyList(mcontext));

			questionAdapter.notifyDataSetChanged();
			answerAdapter.notifyDataSetChanged();
			replyAdapter.notifyDataSetChanged();
			networkObserver.startObservation(this);
		}
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
			// Question
			if (categoryID == 0) {
				mListView.setAdapter(questionAdapter);
			}

			// Answer
			if (categoryID == 1) {
				mListView.setAdapter(answerAdapter);
			}

			// Reply
			if (categoryID == 2) {
				mListView.setAdapter(replyAdapter);
			}
			updateList();
			updateCounter();
			spinAdapter.notifyDataSetChanged();
		}
		/**
		 * Use default sort method is nothing is chosen
		 */
		public void onNothingSelected(AdapterView<?> parent) {
			typeOptionSpinner.setSelection(0);
		}
	}

	class UpdateAuthorThread extends Thread {

		@Override
		public void run() {
			ArrayList<Author> authorList = pushController
					.getWaitingAuthorList(mcontext);
			for (Author author : authorList) {
				String username = author.getUsername();
				if (authorMapController.hasAuthor(mcontext,
						author.getUsername())) {
					Author oldAuthor = authorMapController.getAuthor(username);
					Author newAuthor = updateTheAuthor(oldAuthor, author);
					authorMapManager.updateAuthor(newAuthor);
				} else
					authorMapManager.addAuthor(author);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			runOnUiThread(doUpdateGUIList);
		}

		private Author updateTheAuthor(Author oldAuthor, Author author) {
			ArrayList<Long> oldQuestionId = oldAuthor.getAuthorQuestionId();
			ArrayList<Long> newQuestionId = author.getAuthorQuestionId();
			for (Long questionId : newQuestionId) {
				if (!oldQuestionId.contains(questionId))
					oldAuthor.addAQuestion(questionId);
			}
			return oldAuthor;
		}
	}

	class PostListThread extends Thread {

		@Override
		public void run() {
			waitingQuestionListController.clear();
			waitingAnswerListController.clear();
			waitingReplyListController.clear();

			waitingQuestionListManager.addQuestionList(pushController
					.getWaitingQuestionList(mcontext));
			waitingQuestionListManager.addAnswerList(pushController
					.getWaitingAnswerList(mcontext));
			waitingQuestionListManager.addReplyList(pushController
					.getWaitingReplyList(mcontext));

			pushController.removeWaitingListQuestionList(mcontext);
			pushController.removeWaitingListAnswerList(mcontext);
			pushController.removeWaitingListReplyList(mcontext);

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
