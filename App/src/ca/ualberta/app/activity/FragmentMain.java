package ca.ualberta.app.activity;

import java.util.ArrayList;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionListManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.Toast;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20

public class FragmentMain extends Fragment {
	private QuestionListAdapter adapter = null;
	private QuestionListController questionListController = null;
	private TextView titleBar = null;
	private ListView questionListView = null;
	private QuestionListManager questionListManager;

	// Thread to update adapter after an operation
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.notifyDataSetChanged();
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
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Main");
		questionListView = (ListView) getView().findViewById(
				R.id.question_listView);
	}

	@Override
	public void onStart() {
		super.onStart();
		questionListManager = new QuestionListManager();
		questionListController = new QuestionListController();
		adapter = new QuestionListAdapter(getActivity(),
				R.layout.single_question,
				questionListController.getQuestionArrayList(),
				questionListController.getQuestionList(),getActivity());

		questionListView.setAdapter(adapter);

		// Show details when click on a question
		// questionListView.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int pos,
		// long id) {
		// long questionID = questionListController.getQuestion(pos)
		// .getID();
		//
		// Intent intent = new Intent(mContext, DetailsActivity.class);
		// intent.putExtra(DetailsActivity.MOVIE_ID, questionID);
		//
		// startActivity(intent);
		//
		// }
		//
		// });

		// Delete question on long click
		questionListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Question question = questionListController
								.getQuestion(position);
						Toast.makeText(getActivity().getApplicationContext(),
								"Deleting " + question.getTitle(),
								Toast.LENGTH_LONG).show();

						Thread thread = new DeleteThread(question.getID());
						thread.start();

						return true;
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateList();
	}

	@Override
	public void onPause() {
		super.onPause();
		updateList();
	}

	private void updateList() {
		Thread thread = new SearchThread("");
		thread.start();
	}

	class SearchThread extends Thread {
		// TODO: Implement search thread
		private String search;

		public SearchThread(String s) {
			search = s;

		}

		@Override
		public void run() {
			questionListController.clear();
			questionListController.addAll(questionListManager.searchQuestions(
					search, null));

			getActivity().runOnUiThread(doUpdateGUIList);
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
