package ca.ualberta.app.adapter;

import java.util.ArrayList;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.QuestionListManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionListAdapter extends ArrayAdapter<Question> {
	// private QuestionList localList = null;
	// private QuestionList favList = null;
	private ArrayList<Question> questionList = null;
	// private String FAVLIST = "favList.sav";
	// private String LOCALLIST = "localLList.sav";
	// private Context context;
	private QuestionListManager questionListManager;

	// Thread that close the activity after finishing update

	public QuestionListAdapter(Context context, int textViewResourceId,
			ArrayList<Question> objects, QuestionList questionList) {
		super(context, textViewResourceId, objects);
		this.questionListManager = new QuestionListManager();
		// this.context = context;
		this.questionList = objects;
		// this.localList = new QuestionList();
		// this.favList = new QuestionList();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.single_question, null);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.authorPic = (ImageView) convertView.findViewById(R.id.authorPic);
		holder.authorName = (TextView) convertView
				.findViewById(R.id.authorNameTextView);
		holder.questionTitle = (TextView) convertView
				.findViewById(R.id.questionTitleTextView);
		holder.questionContent = (TextView) convertView
				.findViewById(R.id.questionContentTextView);
		holder.answerState = (TextView) convertView
				.findViewById(R.id.answerStateTextView);
		holder.upvoteState = (TextView) convertView
				.findViewById(R.id.upvoteStateTextView);
		holder.save_Rb = (RadioButton) convertView
				.findViewById(R.id.save_button);
		holder.fav_Rb = (RadioButton) convertView.findViewById(R.id.fav_button);
		holder.upvote_Rb = (RadioButton) convertView
				.findViewById(R.id.upvote_button);
		convertView.setTag(holder);
		Question question = this.getItem(position);

		if (question != null) {
			holder.questionTitle.setText(question.getTitle());
			holder.questionContent.setText(question.getContent());
			holder.authorName.setText(question.getAuthor());
			holder.answerState.setText("Answer: " + question.getAnswerCount());
			holder.upvoteState.setText("Upvote: " + question.getUpvoteCount());
			if (question.ifSelected()) {
				convertView.setBackgroundColor(Color.DKGRAY);
			} else {
				convertView.setBackgroundColor(Color.WHITE);
			}
		}
		holder.upvote_Rb
				.setOnClickListener(new upvoteOnClickListener(position));
		return convertView;
	}

	// private class saveCheckListener implements OnCheckedChangeListener {
	//
	// int position;
	//
	// public saveCheckListener(int position) {
	// this.position = position;
	// }
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView, boolean isSaved)
	// {
	// questionList.getQuestion(position).setSave(isSaved);
	// localList = new QuestionList();
	// for (int i = 0; i < questionList.size(); i++)
	// if (questionList.getQuestion(i).ifSaved())
	// localList.addQuestion(questionList.getQuestion(i));
	// QuestionListController.saveInFile(context, questionList,
	// QUESTIONLIST);
	// QuestionListController.saveInFile(context, localList, LOCALLIST);
	// notifyDataSetChanged();
	// }
	// }
	//
	// private class favCheckListener implements OnCheckedChangeListener {
	//
	// int position;
	//
	// public favCheckListener(int position) {
	// this.position = position;
	// }
	//
	// @Override
	// public void onCheckedChanged(CompoundButton buttonView,
	// boolean isFavorited) {
	// questionList.getQuestion(position).setFavorite(isFavorited);
	// favList = new QuestionList();
	// for (int i = 0; i < questionList.size(); i++)
	// if (questionList.getQuestion(i).ifFavorited())
	// favList.addQuestion(questionList.getQuestion(i));
	// QuestionListController.saveInFile(context, questionList,
	// QUESTIONLIST);
	// QuestionListController.saveInFile(context, favList, FAVLIST);
	// notifyDataSetChanged();
	// }
	// }

	private class upvoteOnClickListener implements OnClickListener {

		int position;

		public upvoteOnClickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Question question = questionList.get(position);
			question.upvote();
			// long questionID = question.getID();
			Thread thread = new UpdateThread(question);
			thread.start();

			notifyDataSetChanged();
		}
	}

	class UpdateThread extends Thread {
		private Question question;

		public UpdateThread(Question question) {
			this.question = question;
		}

		@Override
		public void run() {
			questionListManager.updateQuestion(question);

			// Give some time to get updated info
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ViewHolder {
	ImageView authorPic;
	TextView authorName;
	TextView questionTitle;
	TextView questionContent;
	RadioButton save_Rb;
	RadioButton fav_Rb;
	RadioButton upvote_Rb;
	TextView upvoteState;
	TextView answerState;
}