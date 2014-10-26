package models;

import java.util.ArrayList;
import java.util.List;

//import android.widget.ArrayAdapter;

public class QuestionList {
	
	private ArrayList<Question> questionList=null;
	//private ArrayAdapter<Question> adapter=null;

	//The method will return a boolean corresponds to selection on a question in the question list
	public boolean getStatus() {
		return false;
	}
	
    //The method will return all question in the question list
	public List<Question> getCurrentList() {
		// TODO Auto-generated method stub
		return this.questionList;
	}
}
