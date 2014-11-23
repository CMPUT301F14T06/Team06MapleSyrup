package ca.ualberta.app.test.ActivityTest;

import ca.ualberta.app.activity.CreateAnswerActivity;
import ca.ualberta.app.activity.R;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class CreateAnswerActivityTest extends
		ActivityInstrumentationTestCase2<CreateAnswerActivity> {
	CreateAnswerActivity mActivity;
	EditText content;
	ImageView picture;
	RadioButton submit;
	RadioButton cancel;

	/**
	 * Constructor
	 */
	public CreateAnswerActivityTest() {
		super(CreateAnswerActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		mActivity = getActivity();
		content = (EditText) mActivity
				.findViewById(R.id.answer_content_editText);
		picture = (ImageView) mActivity
				.findViewById(R.id.answer_image_imageView);
		submit = (RadioButton) mActivity
				.findViewById(R.id.submit_answer_button);
		cancel = (RadioButton) mActivity
				.findViewById(R.id.cancel_answer_button);
	}

	/**
	 * Verify Content Layout Parameters
	 * 
	 * @throws Exception
	 */
	@MediumTest
	public void testContentLayout() throws Exception {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, content);
		final ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.width,
				WindowManager.LayoutParams.WRAP_CONTENT);
		assertEquals(layoutParams.height,
				WindowManager.LayoutParams.WRAP_CONTENT);
		tearDown();
	}

	/**
	 * Verify Picture Layout Parameters
	 * 
	 * @throws Exception
	 */
	@MediumTest
	public void testPictureLayout() throws Exception {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, picture);
		final ViewGroup.LayoutParams layoutParams = picture.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.width,
				WindowManager.LayoutParams.WRAP_CONTENT);
		assertEquals(layoutParams.height,
				WindowManager.LayoutParams.WRAP_CONTENT);
		tearDown();
	}

	/**
	 * Verify CommitButton Layout Parameters
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@MediumTest
	public void testSubmitLayout() throws Exception {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, submit);
		final ViewGroup.LayoutParams layoutParams = submit.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.height,
				WindowManager.LayoutParams.FILL_PARENT);
		tearDown();
	}

	/**
	 * Verify CancelButton Layout Parameters
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@MediumTest
	public void testCancleLayout() throws Exception {
		final View decorView = mActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, cancel);
		final ViewGroup.LayoutParams layoutParams = cancel.getLayoutParams();
		assertNotNull(layoutParams);
		assertEquals(layoutParams.height,
				WindowManager.LayoutParams.FILL_PARENT);
		tearDown();
	}

}
