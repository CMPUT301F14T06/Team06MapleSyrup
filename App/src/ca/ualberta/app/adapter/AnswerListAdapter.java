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

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.activity.CreateAnswerReplyActivity;
import ca.ualberta.app.activity.LoginActivity;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.controller.AuthorMapController;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import ca.ualberta.app.thread.UpdateAnswerThread;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter for the answer list, used to display all answers to a question.
 */
public class AnswerListAdapter extends BaseExpandableListAdapter {
	private ArrayList<Answer> answerList = null;
	private Question question;
	private Context context;
	private AuthorMapController authorMapController;
	private CacheController cacheController;
	private Bitmap image = null;
	private Bitmap imageThumb = null;
	private ViewHolder_answer answerHolder = null;
	private ViewHolder_reply replyHolder = null;
	private String loginCause = "Upvote";
	private String loginCause1 = "Reply";

	/**
	 * Constructs the adapter and initializes its context.
	 * 
	 * @param context
	 *            The Context in which the adapter is running.
	 * @param singleAnswer
	 *            One answer in a answer.
	 * @param singleReply
	 *            One reply of an answer in the answer list.
	 * @param answers
	 *            The answer list.
	 * @param question
	 *            The question that the answer list belongs to.
	 */
	public AnswerListAdapter(Context context, int singleAnswer,
			int singleReply, ArrayList<Answer> answers, Question question) {
		this.context = context;
		this.answerList = answers;
		this.question = question;
		cacheController = new CacheController(context);
		authorMapController = new AuthorMapController(context);
	}

	/**
	 * Return the size of the answer.
	 * 
	 * @return The size of the answer.
	 */
	@Override
	public int getGroupCount() {
		return answerList.size();
	}

	/**
	 * return the size of the replies in the answer.
	 * 
	 * @param groupPosition
	 *            the position of the answer.
	 * 
	 * @return the size of the replies in the answer.
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		return answerList.get(groupPosition).getReplyArrayList().size();
	}

	/**
	 * Return answer at the given position.
	 * 
	 * @param groupPosition
	 *            the position of the answer.
	 * 
	 * @return answer at the given position
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return answerList.get(groupPosition);
	}

	/**
	 * Return one reply list of the answer in the given position.
	 * 
	 * @param groupPosition
	 *            the position of the answer.
	 * 
	 * @param childPosition
	 *            the position of the reply list in the answer list.
	 * 
	 * @return one reply list of the answer in the given position.
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return answerList.get(groupPosition).getReplyArrayList()
				.get(childPosition);
	}

	/**
	 * Return the position of the answer list in the question detail list.
	 * 
	 * @param groupPosition
	 *            the position of the answer list.
	 * 
	 * @return groupPosition the position of the answer list.
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * Return the position of the
	 * 
	 * @param groupPosition
	 *            the position of the current answer list.
	 * @param childPosition
	 *            the position of a given reply list in the answer list.
	 * 
	 * @return childPosition the position of a given reply list in the answer
	 *         list.
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * Indicates whether the child and group IDs are stable across changes to
	 * the underlying data.
	 */
	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * Gets a View that displays the given view holder for the specific values
	 * in an answer.
	 * 
	 * @param groupPosition
	 *            The position of the answer.
	 * @param isExpanded
	 *            To indicate if the group view of the answer is expandable.
	 * @param convertView
	 *            A previous recycled view.
	 * @param parent
	 *            Parent view
	 * 
	 * @return The View.
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			answerHolder = new ViewHolder_answer();
			convertView = inflater.inflate(R.layout.single_answer, null);
		} else {
			answerHolder = (ViewHolder_answer) convertView.getTag();
		}

		answerHolder.image = (ImageView) convertView
				.findViewById(R.id.answerImage);
		answerHolder.authorName = (TextView) convertView
				.findViewById(R.id.authorNameTextView);
		answerHolder.answerContent = (TextView) convertView
				.findViewById(R.id.answerContentTextView);
		answerHolder.upvoteState = (TextView) convertView
				.findViewById(R.id.upvoteStateTextView);
		answerHolder.upvote_Rb = (RadioButton) convertView
				.findViewById(R.id.upvote_button);
		answerHolder.timestamp = (TextView) convertView
				.findViewById(R.id.answer_time_textView);
		answerHolder.image.setVisibility(View.GONE);
		answerHolder.reply_Rb = (RadioButton) convertView
				.findViewById(R.id.answer_reply_button);
		checkInternet();
		convertView.setTag(answerHolder);
		Answer answer = answerList.get(groupPosition);

		if (answer != null) {
			Long userId = question.getUserId();
			AuthorMap authorMap = authorMapController.getAuthorMap(context);
			answerHolder.answerContent.setText(answer.getContent());
			answerHolder.authorName.setText(authorMap.getUsername(userId));
			answerHolder.timestamp.setText(answer.getTimestamp().toString());
			answerHolder.upvoteState.setText("Upvote: "
					+ answer.getAnswerUpvoteCount());
			if (User.loginStatus)
				if (answer.hasUpvotedBy(User.author.getUserId()))
					answerHolder.upvote_Rb.setChecked(true);
				else
					answerHolder.upvote_Rb.setChecked(false);

			if (answer.hasImage()) {
				byte[] imageByteArray = Base64.decode(answer.getImage(),
						Base64.NO_WRAP);
				image = BitmapFactory.decodeByteArray(imageByteArray, 0,
						imageByteArray.length);
				scaleImage();
				answerHolder.image.setVisibility(View.VISIBLE);
				answerHolder.image.setImageBitmap(imageThumb);
			}
		}
		answerHolder.image.setOnClickListener(new ViewImageOnClickListener(
				groupPosition));
		answerHolder.upvote_Rb.setOnClickListener(new UpvoteOnClickListener(
				groupPosition));
		answerHolder.reply_Rb.setOnClickListener(new AddReplyOnClickListener(
				groupPosition, answer.getID()));
		ImageView expandIndicator = (ImageView) convertView
				.findViewById(R.id.expandIndicator);
		TextView replyText = (TextView) convertView
				.findViewById(R.id.answer_reply_textView);
		replyText.setText("Reply: "
				+ answerList.get(groupPosition).getReplyArrayList().size()
				+ " replies");
		if (isExpanded) {
			expandIndicator.setBackgroundResource(R.drawable.br_up_icon);
		} else {
			expandIndicator.setBackgroundResource(R.drawable.br_down_icon);
		}
		return convertView;
	}

	private static final int THUMBIMAGESIZE = 100;

	private void scaleImage() {
		// Scale the pic if it is too large:

		if (image.getWidth() > THUMBIMAGESIZE
				|| image.getHeight() > THUMBIMAGESIZE) {
			double scalingFactor = image.getWidth() / THUMBIMAGESIZE;
			if (image.getHeight() > image.getWidth()) {
				scalingFactor = image.getHeight() / THUMBIMAGESIZE;

			}
			int newWidth = (int) Math.round(image.getWidth() / scalingFactor);
			int newHeight = (int) Math.round(image.getHeight() / scalingFactor);
			imageThumb = Bitmap.createScaledBitmap(image, newWidth, newHeight,
					false);
		} else {
			imageThumb = image;
		}

	}

	/**
	 * 
	 */
	private void checkInternet() {
		if (InternetConnectionChecker.isNetworkAvailable(context)) {
			answerHolder.upvote_Rb.setEnabled(true);
		} else {
			answerHolder.upvote_Rb.setEnabled(false);
		}
	}

	/**
	 * Gets a View that displays the given view holder for the specific values
	 * in a reply.
	 * 
	 * @param groupPosition
	 *            The position of the answer.
	 * @param childPosition
	 *            The position of the reply.
	 * @param isLastChild
	 *            To indicate if the reply is the last one in the reply list.
	 * @param convertView
	 *            A previous recycled view.
	 * @param parent
	 *            Parent view
	 * 
	 * @return The View
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			replyHolder = new ViewHolder_reply();
			convertView = inflater.inflate(R.layout.single_reply, null);
		} else {
			replyHolder = (ViewHolder_reply) convertView.getTag();
		}
		replyHolder.authorName = (TextView) convertView
				.findViewById(R.id.replyAuthor_textView);
		replyHolder.replyContent = (TextView) convertView
				.findViewById(R.id.reply_textView);
		replyHolder.timestamp = (TextView) convertView
				.findViewById(R.id.reply_time_textView);
		convertView.setTag(replyHolder);
		Reply reply = answerList.get(groupPosition).getReplyArrayList()
				.get(childPosition);
		if (reply != null) {
			Long userId = question.getUserId();
			AuthorMap authorMap = authorMapController.getAuthorMap(context);
			replyHolder.replyContent.setText(reply.getContent());
			replyHolder.authorName.setText(authorMap.getUsername(userId));
			replyHolder.timestamp.setText(reply.getTimestamp().toString());
		}
		return convertView;
	}

	/**
	 * Whether the child at the specified position is selectable.
	 * 
	 * @param groupPosition
	 *            the position of the answer.
	 * @param childPosition
	 *            the position of the reply.
	 * 
	 * @return whether the child at the specified position is selectable.
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * Setup the listener for the up vote button
	 * 
	 * @author Bicheng
	 * 
	 */
	private class UpvoteOnClickListener implements OnClickListener {

		int position;

		/**
		 * The constructor of the class
		 * 
		 * @param position
		 *            the position of the answer.
		 */
		public UpvoteOnClickListener(int position) {
			this.position = position;
		}

		/**
		 * If the answer is up voted, then add the counter of the vote, and
		 * update it in the data set.
		 * 
		 * @param v
		 *            The view of the up vote button.
		 */
		@Override
		public void onClick(View v) {
			if (User.loginStatus) {
				Answer answer = answerList.get(position);
				answer.upvoteAnswer();
				question.calcCurrentTotalScore();
				Thread thread = new UpdateAnswerThread(question, answer);
				thread.start();
				cacheController.updateFavQuestions(context, question);
				cacheController.updateLocalQuestions(context, question);

				notifyDataSetChanged();
			} else {
				Toast.makeText(v.getContext(), "Login to upvote",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, LoginActivity.class);
				intent.putExtra(LoginActivity.LOGINCAUSE, loginCause);
				context.startActivity(intent);
			}

		}
	}

	/**
	 * Setup the listener for the reply button
	 * 
	 * @author Bicheng
	 */
	private class AddReplyOnClickListener implements OnClickListener {

		int position;
		long answerID;

		/**
		 * The constructor of the class.
		 * 
		 * @param position
		 *            the position of the answer.
		 */
		public AddReplyOnClickListener(int position, long answerID) {
			this.position = position;
			this.answerID = answerID;
		}

		/**
		 * If the button is clicked, act the activity for adding reply
		 * 
		 * @param v
		 *            The view of the reply button.
		 */
		@Override
		public void onClick(View v) {

			if (User.loginStatus) {
				Intent intent = new Intent(context,
						CreateAnswerReplyActivity.class);
				intent.putExtra(CreateAnswerReplyActivity.QUESTION_ID,
						question.getID());
				intent.putExtra(CreateAnswerReplyActivity.QUESTION_TITLE,
						question.getTitle());
				intent.putExtra(CreateAnswerReplyActivity.ANSWER_ID, answerID);
				intent.putExtra(CreateAnswerReplyActivity.ANSWER_POS, position);
				context.startActivity(intent);
				notifyDataSetChanged();
			} else {
				Intent intent = new Intent(context, LoginActivity.class);
				intent.putExtra(LoginActivity.LOGINCAUSE, loginCause1);
				context.startActivity(intent);
			}
		}
	}

	// http://www.csdn123.com/html/mycsdn20140110/2d/2d3c6d5adb428b6708901f7060d31800.html
	private class ViewImageOnClickListener implements OnClickListener {
		int position;

		/**
		 * The constructor of the class
		 * 
		 * @param position
		 *            the position of the answer.
		 */
		public ViewImageOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			Answer answer = answerList.get(position);
			byte[] imageByteArray = Base64.decode(answer.getImage(),
					Base64.NO_WRAP);
			Bitmap image = BitmapFactory.decodeByteArray(imageByteArray, 0,
					imageByteArray.length);
			LayoutInflater inflater = LayoutInflater.from(context);
			View imgEntryView = inflater.inflate(R.layout.dialog_photo, null);
			final AlertDialog dialog = new AlertDialog.Builder(context)
					.create();
			ImageView img = (ImageView) imgEntryView
					.findViewById(R.id.large_image);
			img.setImageBitmap(image);
			dialog.setView(imgEntryView);
			dialog.show();
			imgEntryView.setOnClickListener(new OnClickListener() {
				public void onClick(View paramView) {
					dialog.cancel();
				}
			});
		}
	}

	public Answer getAnswer(int position) {
		return answerList.get(position);
	}

	/**
	 * The container of the views for a single reply
	 * 
	 * @author Bicheng
	 * 
	 */
	class ViewHolder_reply {
		TextView authorName;
		TextView replyContent;
		TextView timestamp;
	}

	/**
	 * The container of the views for a single answer
	 * 
	 * @author Bicheng
	 * 
	 */
	class ViewHolder_answer {
		TextView authorName;
		TextView answerContent;
		RadioButton upvote_Rb;
		TextView timestamp;
		TextView upvoteState;
		ImageView image;
		ExpandableListView replyList;
		RadioButton reply_Rb;
	}

}
