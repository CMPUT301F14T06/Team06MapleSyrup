package ca.ualberta.app.activity.ComparatorTest;

import ca.ualberta.app.activity.MyFavoriteActivity;
import ca.ualberta.app.comparator.DateComparator;
import ca.ualberta.app.models.Question;
import android.test.ActivityInstrumentationTestCase2;

/**
 * JUnit test cases for DateComparator.
 */
public class DateComparatorTest extends
		ActivityInstrumentationTestCase2<MyFavoriteActivity> {

	/**
	 * Constructor
	 */
	public DateComparatorTest() {
		super(MyFavoriteActivity.class);
	}

	/**
	 * Test whether the compare method returns the correct value.
	 * First, create two Questions and a DateComparator with a center location.
	 * Then, check if the compare method returns the correct value when Questions
	 * created at different time are passed in.
	 * Methods tested: compare
	 * 
	 * @throws Exception
	 */
	public void testCompare() throws Exception {
		DateComparator dateComparator = new DateComparator();
		Question question2 = new Question("TestContent2", "TestUserName2",
				"TestTitle2", null);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Question question1 = new Question("TestContent1", "TestUserName1",
				"TestTitle1", null);
		assertEquals(-1, dateComparator.compare(question1, question2));
		assertEquals(1, dateComparator.compare(question2, question1));
		tearDown();
	}

}
