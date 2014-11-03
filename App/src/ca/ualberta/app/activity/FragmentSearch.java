package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.models.QuestionListController;
import ca.ualberta.app.models.QuestionListManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20
public class FragmentSearch extends Fragment {
	private TextView titleBar;
	private EditText searchEditText;
	private QuestionListController questionListController;
	private QuestionListManager questionListManager;
	private QuestionListAdapter adapter = null;
	private ListView searchResultListView;
	private Button searchButton;
	
	// Thread to update adapter after an operation
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.notifyDataSetChanged();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Search");
		searchEditText = (EditText) getView().findViewById(R.id.searchQuestion_EditText);
		searchResultListView = (ListView) getView().findViewById(R.id.searchQuestion_ListView);
		searchButton = (Button) getView().findViewById(R.id.searchQuestion_Button);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		questionListManager = new QuestionListManager();
		questionListController = new QuestionListController();
		adapter = new QuestionListAdapter(getActivity(),
				R.layout.single_question,
				questionListController.getQuestionArrayList(),
				questionListController.getQuestionList());

		searchResultListView.setAdapter(adapter);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				questionListController.clear();
				String searchString = searchEditText.getText().toString();
				searchEditText.setText("");
				Thread thread = new SearchThread(searchString);
				thread.start();	
			}
		});
	}
	
	class SearchThread extends Thread {
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
}