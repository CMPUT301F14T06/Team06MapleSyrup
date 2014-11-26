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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity {
	private Fragment[] fragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioGroup bottom_Rg;
	private RadioButton main_Rb, add_Rb;

	// private InputMethodManager imm;
	// private IBinder windowToken;

	// private int lastCheckedId = R.id.main_menu_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		main_Rb = (RadioButton) findViewById(R.id.main_menu_button);
		add_Rb = (RadioButton) findViewById(R.id.add_menu_button);
		//windowToken = getCurrentFocus().getWindowToken();
		fragments = new Fragment[2];
		fragmentManager = getSupportFragmentManager();
		fragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		fragments[1] = fragmentManager.findFragmentById(R.id.fragement_profile);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(fragments[0]).hide(fragments[1]);
		fragmentTransaction.show(fragments[0]).commit();
		setFragmentIndicator();
	}

	private void setFragmentIndicator() {

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		bottom_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(fragments[0]).hide(fragments[1]);

				switch (checkedId) {
				case R.id.main_menu_button:

					//imm.hideSoftInputFromWindow(windowToken, 0);
					fragmentTransaction.show(fragments[0]).commit();
					break;

				case R.id.add_menu_button:
					//imm.hideSoftInputFromWindow(windowToken, 0);
					main_Rb.performClick();
					add_Rb.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this,
									CreateQuestionActivity.class);
							startActivity(intent);
						}
					});
					break;

				case R.id.profile_menu_button:
					//imm.hideSoftInputFromWindow(windowToken, 0);
					fragmentTransaction.show(fragments[1]).commit();
					break;

				default:
					break;
				}

			}
		});
	}

}