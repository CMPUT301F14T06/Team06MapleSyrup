package ca.ualberta.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Reply;

public class ReplyWaitingListAdapter extends ArrayAdapter<Reply>{
	private ArrayList<Reply> replyList = null;
	ViewHolder holder = null;
	String questionTitle;

	public ReplyWaitingListAdapter(Context context, int textViewResourceId,
			ArrayList<Reply> objects) {
		super(context, textViewResourceId, objects);
		this.replyList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.single_waiting_reply, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.questionTitle = (TextView) convertView
				.findViewById(R.id.waiting_reply_title_textView);
		holder.replyContent = (TextView) convertView
				.findViewById(R.id.waiting_reply_content_textView);
		holder.timestamp = (TextView) convertView
				.findViewById(R.id.waiting_reply_timeStamp_textView);
		convertView.setTag(holder);
		Reply reply = this.getItem(position);

		if (reply != null) {
			holder.questionTitle.setText(reply.getQuestionTitle());
			holder.replyContent.setText(reply.getContent());
			holder.timestamp.setText(reply.getTimestamp().toString());
		}
		return convertView;
	}

	class ViewHolder {
		TextView questionTitle;
		TextView replyContent;
		TextView timestamp;
	}
}
