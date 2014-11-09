/*
 * Copyright 2014 Anni Dai
 * Copyright 2014 Bicheng Yan
 * Copyright 2014 Liwen Chen
 * Copyright 2014 Liang Jingjing
 * Copyright 2014 Xiaocong Zhou
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.app.adapter;

import java.util.ArrayList;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.comparator.AnswerUpvoteComparator;
import ca.ualberta.app.comparator.DateComparator;
import ca.ualberta.app.comparator.PictureComparator;
import ca.ualberta.app.comparator.QuestionUpvoteComparator;
import ca.ualberta.app.comparator.ScoreComparator;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Question;

import ca.ualberta.app.models.User;

import ca.ualberta.app.thread.UpdateQuestionThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionListAdapter extends ArrayAdapter<Question> {
	private ArrayList<Question> questionList = null;
	private CacheController cacheController;
	private String sortingOption = null;
	private String lastSortingOption = null;
	ViewHolder holder = null;

	// Thread that close the activity after finishing update

	public QuestionListAdapter(Context context, int textViewResourceId,
			ArrayList<Question> objects) {
		super(context, textViewResourceId, objects);
		this.questionList = objects;
		cacheController = new CacheController(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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
		holder.timestamp = (TextView) convertView
				.findViewById(R.id.timeTextView);
		convertView.setTag(holder);
		Question question = this.getItem(position);
		
		if (question != null) {
			holder.questionTitle.setText(question.getTitle());
			holder.questionContent.setText(question.getContent());
			holder.authorName.setText(question.getAuthor());
			holder.timestamp.setText(question.getTimestamp().toString());
			holder.answerState.setText("Answer: " + question.getAnswerCount());
			holder.upvoteState.setText("Upvote: "
					+ question.getQuestionUpvoteCount());
			if (cacheController.hasSaved(getContext(), question))
				holder.save_Rb.setChecked(true);
			else
				holder.save_Rb.setChecked(false);
			if (cacheController.hasFavorited(getContext(), question))
				holder.fav_Rb.setChecked(true);
			else
				holder.fav_Rb.setChecked(false);
			cacheController.updateFavQuestions(getContext(), question);
			cacheController.updateLocalQuestions(getContext(), question);
		}
		holder.save_Rb.setOnClickListener(new saveOnClickListener(position));
		holder.fav_Rb.setOnClickListener(new favOnClickListener(position));
		holder.upvote_Rb
				.setOnClickListener(new upvoteOnClickListener(position));
		return convertView;
	}

	private class saveOnClickListener implements OnClickListener {

		int position;

		public saveOnClickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Question question = questionList.get(position);
			if (cacheController.hasSaved(getContext(), question)) {
				cacheController.removeLocalQuestions(getContext(), question);
				holder.save_Rb.setChecked(false);
			} else {
				cacheController.addLocalQuestions(getContext(), question);
				holder.save_Rb.setChecked(true);

			}
			notifyDataSetChanged();
		}
	}

	private class favOnClickListener implements OnClickListener {

		int position;

		public favOnClickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Question question = questionList.get(position);
			if (cacheController.hasFavorited(getContext(), question)) {
				cacheController.removeFavQuestions(getContext(), question);
				holder.fav_Rb.setChecked(false);

			} else {
				cacheController.addFavQuestions(getContext(), question);
				holder.fav_Rb.setChecked(true);
			}
			notifyDataSetChanged();
		}
	}

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
			question.upvoteQuestion();
			question.calcCurrentTotalScore();
			// long questionID = question.getID();
			Thread thread = new UpdateQuestionThread(question);
			thread.start();
			sortingOption = lastSortingOption;
			cacheController.updateFavQuestions(getContext(), question);
			cacheController.updateLocalQuestions(getContext(), question);
			applySortMethod();
			notifyDataSetChanged();
		}
	}

	public void applySortMethod() {
		if (sortingOption == null) {
			sortingOption = lastSortingOption;
		}
		if (sortingOption.equals("Sort By Picture")) {
			this.sort(new PictureComparator());
		}
		if (sortingOption.equals("Sort By Date")) {
			this.sort(new DateComparator());
		}
		if (sortingOption.equals("Sort By Score")) {
			this.sort(new ScoreComparator());
		}
		if (sortingOption.equals("Sort By Question Upvote")) {
			this.sort(new QuestionUpvoteComparator());
		}
		if (sortingOption.equals("Sort By Answer Upvote")) {
			this.sort(new AnswerUpvoteComparator());
		}
		this.lastSortingOption = sortingOption;
		sortingOption = null;
	}

	/**
	 * Set the current sorting option.
	 * 
	 * @param option
	 *            : a String which is one of the sorting options.
	 */
	public void setSortingOption(String option) {

		this.sortingOption = option;
	}

}

/**
 * class ViewHolder
 * 
 * @author Anni
 * 
 */
class ViewHolder {
	ImageView authorPic;
	TextView authorName;
	TextView questionTitle;
	TextView questionContent;
	RadioButton save_Rb;
	RadioButton fav_Rb;
	RadioButton upvote_Rb;
	TextView timestamp;
	TextView upvoteState;
	TextView answerState;
}