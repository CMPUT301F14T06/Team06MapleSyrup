package ca.ualberta.app.adapter;

import java.util.ArrayList;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.comparator.DateComparator;
import ca.ualberta.app.models.Question;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for the question list in waiting list, used to display all un-posted
 * questions.
 */
public class QuestionWaitingListAdapter extends ArrayAdapter<Question> {
	private ArrayList<Question> questionList = null;
	ViewHolder holder = null;

	/**
	 * Constructs the adapter and initializes its context.
	 * 
	 * @param context
	 *            The Context in which the adapter is running.
	 * @param textViewResourceId
	 *            Format of an question view
	 * @param objects
	 *            The question list.
	 */
	public QuestionWaitingListAdapter(Context context, int textViewResourceId,
			ArrayList<Question> objects) {
		super(context, textViewResourceId, objects);
		this.questionList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.single_waiting_question,
					null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.questionTitle = (TextView) convertView
				.findViewById(R.id.waiting_question_title_textView);
		holder.questionContent = (TextView) convertView
				.findViewById(R.id.waiting_question_content_textView);
		holder.timestamp = (TextView) convertView
				.findViewById(R.id.waiting_question_timeStamp_textView);
		convertView.setTag(holder);
		Question question = this.getItem(position);

		if (question != null) {
			holder.questionTitle.setText(question.getTitle());
			holder.questionContent.setText(question.getContent());
			holder.timestamp.setText(question.getTimestamp().toString());
		}
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		this.setNotifyOnChange(false);
		this.sort(new DateComparator());
		this.setNotifyOnChange(true);
		super.notifyDataSetChanged();
	}

	/**
	 * The container of the views for a single question
	 * 
	 * @author Bicheng
	 * 
	 */
	class ViewHolder {
		TextView questionTitle;
		TextView questionContent;
		TextView timestamp;
	}
}
