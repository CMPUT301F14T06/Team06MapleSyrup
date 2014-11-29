//package ca.ualberta.app.test.ActivityTest;
//
//import ca.ualberta.app.activity.MyFavoriteActivity;
//import ca.ualberta.app.view.ScrollListView;
//import android.test.ActivityInstrumentationTestCase2;
//import android.test.ViewAsserts;
//import android.test.suitebuilder.annotation.MediumTest;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Spinner;
//import ca.ualberta.app.activity.R;
//
//public class MyFavoriteActivityTest extends
//		ActivityInstrumentationTestCase2<MyFavoriteActivity> {
//	MyFavoriteActivity mActivity;
//	Spinner mSpinner;
//	ScrollListView mListView;
//
//	/**
//	 * Constructor
//	 */
//	public MyFavoriteActivityTest() {
//		super(MyFavoriteActivity.class);
//	}
//
//	/**
//	 * Sets up the test environment before each test.
//	 * 
//	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
//	 */
//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//		setActivityInitialTouchMode(true);
//		mActivity = getActivity();
//		mSpinner = (Spinner) mActivity
//				.findViewById(R.id.favSort_spinner);
//		mListView = (ScrollListView) mActivity
//				.findViewById(R.id.favQuestion_ListView);
//	}
//
//	/**
//	 * Verify Spinner Layout Parameters
//	 */
//	@MediumTest
//	public void testSpinnerLayout() {
//		final View decorView = mActivity.getWindow().getDecorView();
//		ViewAsserts.assertOnScreen(decorView, mSpinner);
//		final ViewGroup.LayoutParams layoutParams = mSpinner.getLayoutParams();
//		assertNotNull(layoutParams);
//		assertEquals(layoutParams.width,
//				WindowManager.LayoutParams.WRAP_CONTENT);
//		assertEquals(layoutParams.height,
//				WindowManager.LayoutParams.WRAP_CONTENT);
//	}
//
//	/**
//	 * Verify ListView Layout Parameters
//	 */
//	@MediumTest
//	public void testListViewLayout() {
//		final View decorView = mActivity.getWindow().getDecorView();
//		ViewAsserts.assertOnScreen(decorView, mListView);
//		final ViewGroup.LayoutParams layoutParams = mListView.getLayoutParams();
//		assertEquals(layoutParams.height,
//				WindowManager.LayoutParams.WRAP_CONTENT);
//	}
//}
//
