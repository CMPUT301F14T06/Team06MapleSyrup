package ca.ualberta.app.test.ActivityTest;

import ca.ualberta.app.activity.LoginActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import ca.ualberta.app.activity.R;

public class LoginActivityTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {
	LoginActivity mActivity;	
	EditText content;
	RadioButton login;
	RadioButton cancel;
	
	/**
	 * Constructor
	 */
	public LoginActivityTest() {
		super(	LoginActivity.class);
	}

	/**
	 * Sets up the test environment before each test.
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		mActivity = getActivity();
		content = (EditText) mActivity
				.findViewById(R.id.username_editText);
		login = (RadioButton) mActivity
				.findViewById(R.id.login_button);
		cancel = (RadioButton) mActivity
				.findViewById(R.id.login_cancel_button);
	}

	/**
	 * Verify EditText Layout Parameters
	 */
	@MediumTest
	public void testEditTextLayout() {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, content);
		final ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.height,
				WindowManager.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * Verify Login Button Layout Parameters
	 */
	@MediumTest
	public void testLoginButtonLayout() {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, login);
		final ViewGroup.LayoutParams layoutParams = login.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.width,
				WindowManager.LayoutParams.MATCH_PARENT);
	}
	
	/**
	 * Verify Cancel Button Layout Parameters
	 */
	@MediumTest
	public void testCancelButtonLayout() {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, cancel);
		final ViewGroup.LayoutParams layoutParams = cancel.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.width,
				WindowManager.LayoutParams.MATCH_PARENT);
	}
}
