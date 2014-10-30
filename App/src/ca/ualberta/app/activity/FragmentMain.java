package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.models.InputsListController;
import ca.ualberta.app.models.InputsListModel;
import ca.ualberta.app.models.Question;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20

public class FragmentMain extends Fragment {
	private QuestionListAdapter adapter = null;
	private InputsListModel currentQuestionList = null;
	private TextView titleBar = null;
	private ListView questionListView = null;
	private String FILENAME = "questionList.sav";
	private String FAVFILENAME = "favList.sav";
	private String LOCFILENAME = "localList.sav";
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
		currentQuestionList = InputsListController.loadFromFile(getActivity()
				.getApplicationContext(), FILENAME);
		questionListView = (ListView) getView().findViewById(
				R.id.question_listView);
		adapter = new QuestionListAdapter(getActivity(),
				R.layout.single_question, currentQuestionList.getArrayList());
		questionListView.setAdapter(adapter);
		updateList();
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
		currentQuestionList = InputsListController.loadFromFile(getActivity()
				.getApplicationContext(), FILENAME);
		adapter.notifyDataSetChanged();
	}

}
