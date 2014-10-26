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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

//The fragment part is from this web site: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20
public class MainActivity extends FragmentActivity {
	private Fragment[] fragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioGroup bottom_Rg;
	private RadioButton add_button;

	// private RadioButton main_button, search_button, profile_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		// main_button = (RadioButton) findViewById(R.id.main_button);
		add_button = (RadioButton) findViewById(R.id.add_button);
		// search_button = (RadioButton) findViewById(R.id.search_button);
		// profile_button = (RadioButton) findViewById(R.id.profile_button);
		fragments = new Fragment[3];

		fragmentManager = getSupportFragmentManager();
		fragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		fragments[1] = fragmentManager.findFragmentById(R.id.fragement_search);
		fragments[2] = fragmentManager.findFragmentById(R.id.fragement_profile);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(fragments[0]).hide(fragments[1]).hide(fragments[2]);
		fragmentTransaction.show(fragments[0]).commit();

		setFragmentIndicator();
	}

	private void setFragmentIndicator() {

		bottom_Rg = (RadioGroup) findViewById(R.id.main_menu);
		bottom_Rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(fragments[0]).hide(fragments[1])
						.hide(fragments[2]);
				switch (checkedId) {
				case R.id.main_button:
					fragmentTransaction.show(fragments[0]).commit();
					break;

				case R.id.add_button:
					// go to Create Question activity
					add_button.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this,
									CreateInputsActivity.class);
							startActivity(intent);
						}
					});
					
					break;

				case R.id.search_button:
					fragmentTransaction.show(fragments[1]).commit();
					break;

				case R.id.profile_button:
					fragmentTransaction.show(fragments[2]).commit();
					break;

				default:
					break;
				}
			}
		});
	}

}