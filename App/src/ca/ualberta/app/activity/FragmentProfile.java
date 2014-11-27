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

package ca.ualberta.app.activity;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMap;
import ca.ualberta.app.models.AuthorMapIO;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This is the fragment activity for the mean question list, once the app is
 * started, or a user clicks the "Main" button on the bottom action bar
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 */
public class FragmentProfile extends Fragment {
	private TextView titleBar;
	private ImageButton changePhotoButton;
	private TextView setAuthorName;
	private RadioButton local_cache;
	private RadioButton fav_question;
	private RadioButton my_question;
	private RadioButton waiting_list;
	private RadioButton login;
	private RadioButton logout;
	private long from = 0;
	private long size = 1000;
	private String lable = "author";
	private Context mcontext;
	private AuthorMapManager authorMapManager;
	private AuthorMap authorMap;
	private String FILENAME = "AUTHORMAP.sav";
	private String newUsername = null;
	private String loginCause = "Login";
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			checkLoginStatus();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mcontext = getActivity().getApplicationContext();
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Profile");
		changePhotoButton = (ImageButton) getView().findViewById(
				R.id.changePhotoButton);
		setAuthorName = (TextView) getView().findViewById(R.id.setAuthorName);
		local_cache = (RadioButton) getView().findViewById(R.id.local_cache);
		fav_question = (RadioButton) getView().findViewById(R.id.fav_question);
		my_question = (RadioButton) getView().findViewById(R.id.my_question);
		waiting_list = (RadioButton) getView().findViewById(R.id.waiting_list);
		login = (RadioButton) getView().findViewById(R.id.login);
		logout = (RadioButton) getView().findViewById(R.id.logout);

		checkLoginStatus();
		setAuthorName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				authorMap = new AuthorMap();
				authorMapManager = new AuthorMapManager();
				if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
					Thread thread = new SearchThread("");
					thread.start();
					showDialog();
				} else {
					Toast.makeText(mcontext,
							"Internet is required to change name",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				intent.putExtra(LoginActivity.LOGINCAUSE, loginCause);
				startActivity(intent);

			}
		});

		logout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				User.loginStatus = false;
				User.author = null;
				getActivity().recreate();
			}
		});

		my_question.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MyQuestionActivity.class);
				startActivity(intent);

			}
		});

		fav_question.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MyFavoriteActivity.class);
				startActivity(intent);

			}
		});

		local_cache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyLocalActivity.class);
				startActivity(intent);
			}
		});

		waiting_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						WaitingListActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		checkLoginStatus();
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			titleBar.setText("Profile");
		} else {
			titleBar.setText("Profile(Not Connected)");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		checkLoginStatus();
	}

	private void showDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("Change Author Name");
		alert.setMessage("Enter Your Name Here");

		final EditText input = new EditText(getActivity());
		alert.setView(input);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				newUsername = input.getEditableText().toString();
				if(newUsername != null){
				if (!authorMap.hasAuthor(newUsername)) {
					String oldUsername = User.author.getUsername();
					User.author.setUsername(newUsername);

					Thread addThread = new AddThread(User.author);
					addThread.start();
					Thread deleteThread = new DeleteThread(oldUsername);
					deleteThread.start();

				} else {
					Toast.makeText(
							getActivity(),
							"The username is aready exist, please choose another one.",
							Toast.LENGTH_SHORT).show();
					showDialog();
				}
				setAuthorName.setText(User.author.getUsername());
			}else 
				Toast.makeText(mcontext, "Please fill in name", Toast.LENGTH_SHORT).show();
			}

		});
		alert.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	public void checkLoginStatus() {
		if (User.loginStatus) {
			changePhotoButton.setVisibility(View.VISIBLE);
			setAuthorName.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE);
			waiting_list.setVisibility(View.VISIBLE);
			logout.setVisibility(View.VISIBLE);
			my_question.setVisibility(View.VISIBLE);
			setAuthorName.setText(User.author.getUsername());
			authorMapManager = new AuthorMapManager();
			authorMap = new AuthorMap();

		} else {
			changePhotoButton.setVisibility(View.GONE);
			setAuthorName.setVisibility(View.GONE);
			waiting_list.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.GONE);
			my_question.setVisibility(View.GONE);
		}
	}

	class SearchThread extends Thread {
		private String search;

		public SearchThread(String s) {
			search = s;
		}

		@Override
		public void run() {
			authorMap.clear();
			authorMap.putAll(authorMapManager.searchAuthors(search, null, from,
					size, lable));
		}
	}

	class AddThread extends Thread {
		private Author theAuthor;

		public AddThread(Author theAuthor) {
			this.theAuthor = theAuthor;
		}

		@Override
		public void run() {
			User.author = theAuthor;
			authorMap.addAuthor(theAuthor);
			AuthorMapIO.saveInFile(mcontext, authorMap, FILENAME);
			authorMapManager.addAuthor(theAuthor);
			// Give some time to get updated info
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class DeleteThread extends Thread {
		private String username;

		public DeleteThread(String username) {
			this.username = username;
		}

		@Override
		public void run() {
			authorMapManager.deleteAuthor(username);
			authorMap.removeAuthor(username);
			AuthorMapIO.saveInFile(mcontext, authorMap, FILENAME);
			getActivity().runOnUiThread(doUpdateGUIList);
		}
	}

}