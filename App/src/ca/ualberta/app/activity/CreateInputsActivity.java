package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CreateInputsActivity extends Activity {
	private RadioGroup create_menu_Rg;
	private RadioButton submit;
	private RadioButton cancel;
	private RadioButton galary;
	private RadioButton photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_inputs);
		create_menu_Rg = (RadioGroup) findViewById(R.id.create_input_menu);
		submit = (RadioButton) findViewById(R.id.submit_button);
		cancel = (RadioButton) findViewById(R.id.cancel_button);
		photo = (RadioButton) findViewById(R.id.take_pic);
		galary = (RadioButton) findViewById(R.id.add_pic);
		create_menu_Rg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						switch (checkedId) {
						case R.id.submit_button:
							submit.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									Intent intent = new Intent(
											CreateInputsActivity.this,
											MainActivity.class);
									startActivity(intent);
								}
							});
							break;

						case R.id.cancel_button:
							cancel.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									Intent intent = new Intent(
											CreateInputsActivity.this,
											MainActivity.class);
									startActivity(intent);
								}
							});
							break;

						case R.id.take_pic:

							break;

						case R.id.add_pic:

							break;

						default:
							break;
						}

					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_input, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
