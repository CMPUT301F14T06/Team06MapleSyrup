package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.models.InputsListModel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20

public class FragmentMain extends Fragment {
	private TextView titleBar;
	private QuestionListAdapter adapter=null;
	private ListView questionList=null;
	private InputsListModel currentQuestionList = null;
	private ListView questionListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//questionList=(ListView) getView().findViewById(R.id.questionList);
		currentQuestionList = new InputsListModel();
    	//adapter=new QuestionListAdapter(getActivity(),R.layout.single_question,currentQuestionList.getArrayList());
    	//questionList.setAdapter((ListAdapter) adapter);
		return inflater.inflate(R.layout.fragment_main, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Main");
		questionListView = (ListView) getView().findViewById(R.id.question_listView);
		
	}
}
