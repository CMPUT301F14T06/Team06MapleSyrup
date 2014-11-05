package ca.ualberta.app.activity;

import ca.ualberta.app.activity.FragmentMain.DeleteThread;
import ca.ualberta.app.activity.FragmentMain.GetQuestionThread;
import ca.ualberta.app.activity.FragmentMain.SearchThread;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.QuestionListManager;
import ca.ualberta.app.models.User;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

	private String MYQUESTION = User.author.getUsername() + ".sav";
	private Question question;
	private QuestionListAdapter adapter = null;
	private QuestionListController myQuestionListController;
	private QuestionListManager questionListManager;
	private TextView titleBar = null;
	private ListView myquestionListView = null;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	public String sortString = "date";
	// Thread to update adapter after an operation

	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_question);
		mcontext = this;
		titleBar = (TextView) findViewById(R.id.titleTv);
		titleBar.setText("My Question List");
		myquestionListView = (ListView) findViewById(R.id.my_question_ListView);
		sortOptionSpinner = (Spinner) findViewById(R.id.my_question_sort_spinner);
		myQuestionListController.addAll(QuestionListController.loadFromFile(
				this, MYQUESTION));
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				myQuestionListController.getQuestionArrayList());
		spin_adapter = new ArrayAdapter<String>(mcontext,
				R.layout.spinner_item, sortOption);

		myquestionListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		// Show details when click on a question
		myquestionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionID = myQuestionListController.getQuestion(pos)
						.getID();

				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);

				startActivity(intent);

			}

		});

		// Delete question on long click
		myquestionListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						Question question = myQuestionListController
								.getQuestion(position);

						if (User.author != null
								&& User.author.getUsername().equals(
										question.getAuthor())) {
							Toast.makeText(
									mcontext,
									"Deleting the Question: "
											+ question.getTitle(),
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

	}

	private class change_category_click implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			categoryID = position;

			// sort by Date
			if (categoryID == 0) {
				sortString = "date";
				// updateList();
			}

			// sort by Picture
			if (categoryID == 1) {
				sortString = "score";
				// updateList();
			}

			// sort by Score
			if (categoryID == 2) {
				sortString = "picture";
				adapter.setSortingOption(sortByPicture);
				// updateList();
			}
			// sort by Question upvote
			if (categoryID == 3) {
				sortString = "q_upvote";
				// updateList();
			}

			// sort by Answer upvote
			if (categoryID == 4) {
				sortString = "a_upvote";
				// updateList();
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
	}

	private void updateList() {

		myQuestionListController.clear();

		for (long questionId : User.author.getAuthorQuestionId()) {
			Thread getThread = new GetQuestionThread(questionId);
			getThread.start();
			myQuestionListController.addQuestion(question);
		}
	}

	class GetQuestionThread extends Thread {
		// TODO: Implement search thread
		private long questionId;

		public GetQuestionThread(long questionId) {
			this.questionId = questionId;
		}

		@Override
		public void run() {
			question = questionListManager.getQuestion(questionId);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_question, menu);
		return true;
	}

}
