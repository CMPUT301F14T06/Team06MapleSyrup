package ca.ualberta.app.adapter;

import java.util.ArrayList;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Question;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuestionListAdapter extends ArrayAdapter<Question> {

	public QuestionListAdapter(Context context, int textViewResourceId, ArrayList<Question> objects) {
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			convertView = inflater.inflate(R.layout.single_question,null);
		}
		
		Question question=this.getItem(position);
		
		if(question!=null){
			TextView questionTitleText=(TextView)convertView.findViewById(R.id.questionTitleTextView);
			questionTitleText.setText("a question");
			
			//if(question.getStatus()){
			//	convertView.setBackgroundColor(Color.CYAN);
			//}
			//else{
			//	convertView.setBackgroundColor(Color.WHITE);
			//}
			
		}
		
		return convertView;
	}

}
