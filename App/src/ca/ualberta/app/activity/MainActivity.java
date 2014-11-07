package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * This is the mian active that contains 5 mean functionalities by using fragments: (1) view all questions authors have posted; (2) view users' favorite questions;
 * (3) login as an author, and create a new question; (4) search questions which contain a given keyword; (5) view and modify user profiles.
 * 
 * The fragment part is from this web site: http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni, Bicheng, Xiaocong
 */
public class MainActivity extends FragmentActivity {
	private Fragment[] fragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioGroup bottom_Rg;
	private RadioButton main_Rb, fav_Rb, add_Rb, search_Rb, profile_Rb;
	private Boolean loginStatus;

	// private int lastCheckedId = R.id.main_menu_button;


	/**
	* onCreate method.
	* Once the activity is created, first set the content view, and initialize ActionBar for fragments, and a Spinner for sort options.
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		main_Rb = (RadioButton) findViewById(R.id.main_menu_button);
		fav_Rb = (RadioButton) findViewById(R.id.fav_menu_button);
		add_Rb = (RadioButton) findViewById(R.id.add_menu_button);
		search_Rb = (RadioButton) findViewById(R.id.search_menu_button);
		profile_Rb = (RadioButton) findViewById(R.id.profile_menu_button);
		fragments = new Fragment[4];
		fragmentManager = getSupportFragmentManager();
		fragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		fragments[1] = fragmentManager.findFragmentById(R.id.fragement_fav);
		fragments[2] = fragmentManager.findFragmentById(R.id.fragement_search);
		fragments[3] = fragmentManager.findFragmentById(R.id.fragement_profile);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(fragments[0]).hide(fragments[1]).hide(fragments[2])
				.hide(fragments[3]);
		fragmentTransaction.show(fragments[0]).commit();
		checkLoginStatus();
		setFragmentIndicator();
	}
	
	/**
	 * Connect all fragment activities to the corresponding buttons in the action bar at the bottom of the screen.
	 * Hide the "ask a question" fragment, until the user login as an author
	 */
	private void setFragmentIndicator() {

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		bottom_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(fragments[0]).hide(fragments[1])
						.hide(fragments[2]).hide(fragments[3]);
				switch (checkedId) {
				case R.id.main_menu_button:
					fragmentTransaction.show(fragments[0]).commit();
					break;

				case R.id.fav_menu_button:
					fragmentTransaction.show(fragments[1]).commit();
					break;

				case R.id.add_menu_button:
					main_Rb.performClick();
					add_Rb.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this,
									CreateQuestionActivity.class);
							startActivity(intent);
						}
					});
					break;

				case R.id.search_menu_button:
					fragmentTransaction.show(fragments[2]).commit();
					break;

				case R.id.profile_menu_button:
					fragmentTransaction.show(fragments[3]).commit();
					break;

				default:
					break;
				}
				// if (checkedId != R.id.add_menu_button)
				// lastCheckedId = checkedId;
			}
		});
	}

	/**
	* onResume method.
	* Once the activity is resumed from other activities, check the user's longing statues.
	*/
	@Override
	protected void onResume() {
		super.onResume();
		checkLoginStatus();
	}

	/**
	* onResume method.
	* Once the activity is paused, check the user's longing statues.
	*/
	@Override
	protected void onPause() {
		super.onPause();
		checkLoginStatus();
	}
	
	/**
	* this method is used to check out if an user has logged in: (1) if the user has logged in, then show the "Ask Question" button in the action bar,
	* and allow displaying the CreateQuestionActivity
	*/
	public void checkLoginStatus() {
		loginStatus = User.loginStatus;
		if (loginStatus) {
			add_Rb.setVisibility(View.VISIBLE);
			// add_Rb.setClickable(true);
		} else {
			add_Rb.setVisibility(View.GONE);
			// add_Rb.setClickable(false);
		}
	}

}