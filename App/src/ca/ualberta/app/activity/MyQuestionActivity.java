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

	private QuestionListAdapter adapter = null;
	private QuestionListController myQuestionListController;
	private QuestionListManager questionListManager;
	private QuestionList myQuestionList;
	private ListView myquestionListView = null;
	private Spinner sortOptionSpinner;
	private Context mcontext;
	private ArrayAdapter<String> spin_adapter;
	private static long categoryID;
	public String sortString = "date";

	// Thread to update adapter after an operation
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.applySortMethod();
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_question);
		mcontext = this;
		myquestionListView = (ListView) findViewById(R.id.my_question_ListView);
		sortOptionSpinner = (Spinner) findViewById(R.id.my_question_sort_spinner);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		myQuestionListController = new QuestionListController();
		questionListManager = new QuestionListManager();
		adapter = new QuestionListAdapter(this, R.layout.single_question,
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
			updateList();
		}

		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(0);
		}
	}

	private void updateList() {
		myQuestionListController.clear();
		myQuestionList = questionListManager.getQuestionList(User.author
				.getAuthorQuestionId());
		myQuestionListController.addAll(myQuestionList);
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

}
