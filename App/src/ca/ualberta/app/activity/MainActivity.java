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
 * This is the mean activity that contains 5 mean functionalities by inflating 5
 * fragments: (1) view all questions authors have posted; (2) view users'
 * favorite questions; (3) login as an author, and create a new question; (4)
 * search questions which contain a given keyword; (5) view and modify user
 * profiles.
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 * @author Anni
 * @author Bicheng
 * @author Xiaocong
 */
public class MainActivity extends FragmentActivity {
	private Fragment[] fragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioGroup bottom_Rg;
	private RadioButton main_Rb, add_Rb;
	private String loginCause = "Question";

	/**
	 * onCreate method. Once the activity is created, first set the content
	 * view, and initialize ActionBar for fragments, and a Spinner for sort
	 * options.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state bundle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		main_Rb = (RadioButton) findViewById(R.id.main_menu_button);
		add_Rb = (RadioButton) findViewById(R.id.add_menu_button);
		fragments = new Fragment[2];
		fragmentManager = getSupportFragmentManager();
		fragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		fragments[1] = fragmentManager.findFragmentById(R.id.fragement_profile);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(fragments[0]).hide(fragments[1]);
		fragmentTransaction.show(fragments[0]).commit();
		setFragmentIndicator();
	}

	/**
	 * Connecting all fragment activities to the corresponding buttons in the
	 * action bar at the bottom of the screen. Hiding the "ask a question"
	 * fragment, until the user login as an author
	 */
	private void setFragmentIndicator() {

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		bottom_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(fragments[0]).hide(fragments[1]);

				switch (checkedId) {
				case R.id.main_menu_button:
					fragmentTransaction.show(fragments[0]).commit();
					break;

				case R.id.add_menu_button:
					main_Rb.performClick();
					add_Rb.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (!User.loginStatus) {
								Intent intent = new Intent(MainActivity.this,
										LoginActivity.class);
								intent.putExtra(LoginActivity.LOGINCAUSE,
										loginCause);
								startActivity(intent);
							} else {
								Intent intent = new Intent(MainActivity.this,
										CreateQuestionActivity.class);
								startActivity(intent);
							}
						}
					});
					break;

				case R.id.profile_menu_button:
					fragmentTransaction.show(fragments[1]).commit();
					break;

				default:
					break;
				}

			}
		});
	}
}