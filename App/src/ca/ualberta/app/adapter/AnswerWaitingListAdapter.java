package ca.ualberta.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Answer;

/**
 * Adapter for the answer list in waiting list, used to display all un-posted
 * answers.
 */
public class AnswerWaitingListAdapter extends ArrayAdapter<Answer>{
	private ArrayList<Answer> answerList = null;
	ViewHolder holder = null;
	String questionTitle;

	/**
	 * Constructs the adapter and initializes its context.
	 * 
	 * @param context
	 *            The Context in which the adapter is running.
	 * @param textViewResourceId
	 *            Format of an answer view
	 * @param objects
	 *            The answer list.
	 */
	public AnswerWaitingListAdapter(Context context, int textViewResourceId,
			ArrayList<Answer> objects) {
		super(context, textViewResourceId, objects);
		this.answerList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.single_waiting_answer, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.questionTitle = (TextView) convertView
				.findViewById(R.id.waiting_answer_title_textView);
		holder.answerContent = (TextView) convertView
				.findViewById(R.id.waiting_answer_content_textView);
		holder.timestamp = (TextView) convertView
				.findViewById(R.id.waiting_answer_timeStamp_textView);
		convertView.setTag(holder);
		Answer answer = this.getItem(position);

		if (answer != null) {
			holder.questionTitle.setText(answer.getQuestionTitle());
			holder.answerContent.setText(answer.getContent());
			holder.timestamp.setText(answer.getTimestamp().toString());
		}
		return convertView;
	}
	
	/**
	 * The container of the views for a single answer
	 * 
	 * @author Bicheng
	 * 
	 */
	class ViewHolder {
		TextView questionTitle;
		TextView answerContent;
		TextView timestamp;
	}
}
