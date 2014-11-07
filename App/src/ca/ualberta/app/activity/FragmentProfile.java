package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * This is the fragment activity for the mean question list, once the app is started, or a user clicks the "Main" button on the bottom action bar
 * 
 * The fragment part is from this web site: http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni
 */
public class FragmentProfile extends Fragment {
	private TextView titleBar;
	private ImageButton changePhotoButton;
	private TextView setAuthorName;
	private RadioGroup profile_menu;
	private RadioButton local_cache;
	private RadioButton fav_question;
	private RadioButton my_question;
	private RadioButton login;
	private RadioButton logout;
	private boolean loginStatus;

	/**
	 * Once the fragment is active, the user interface, R.layout.fragment_profile will be load into the fragment.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	/**
	 * Once the fragment is created, this method will give each view an object to help other methods set data.
	 * Also, the listeners for all buttons will be setup in this method.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Profile");
		changePhotoButton = (ImageButton) getView().findViewById(
				R.id.changePhotoButton);
		setAuthorName = (TextView) getView().findViewById(R.id.setAuthorName);
		profile_menu = (RadioGroup) getView().findViewById(R.id.profile_menu);
		local_cache = (RadioButton) getView().findViewById(R.id.local_cache);
		fav_question = (RadioButton) getView().findViewById(R.id.fav_question);
		my_question = (RadioButton) getView().findViewById(R.id.my_question);
		login = (RadioButton) getView().findViewById(R.id.login);
		logout = (RadioButton) getView().findViewById(R.id.logout);
		checkLoginStatus();
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
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
	}

	/**
	* onResume method.
	* Once the activity is resumed from other activities, check the user's longing statues.
	*/
	@Override
	public void onResume() {
		super.onResume();
		checkLoginStatus();
	}

	/**
	* onResume method.
	* Once the activity is paused, check the user's longing statues.
	*/
	@Override
	public void onPause() {
		super.onPause();
		checkLoginStatus();
	}

	/**
	* this method is used to check out if an user has logged in: (1) if the user has logged in, then show the current user's profile on the screen;
	* (2) if not, show the login window
	*/
	public void checkLoginStatus() {
		loginStatus = User.loginStatus;
		if (loginStatus) {
			changePhotoButton.setVisibility(View.VISIBLE);
			setAuthorName.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE);
			logout.setVisibility(View.VISIBLE);
			my_question.setVisibility(View.VISIBLE);
			setAuthorName.setText(User.author.getUsername());

		} else {
			changePhotoButton.setVisibility(View.GONE);
			setAuthorName.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.GONE);
			my_question.setVisibility(View.GONE);
		}
	}
}