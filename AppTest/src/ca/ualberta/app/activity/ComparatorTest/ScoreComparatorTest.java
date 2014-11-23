package ca.ualberta.app.activity.ComparatorTest;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.app.activity.MyFavoriteActivity;
import ca.ualberta.app.comparator.QuestionUpvoteComparator;
import ca.ualberta.app.models.Question;

public class ScoreComparatorTest extends
		ActivityInstrumentationTestCase2<MyFavoriteActivity> {

	/**
	 * Constructor
	 */
	public ScoreComparatorTest() {
		super(MyFavoriteActivity.class);
	}

	/**
	 * Test whether the compare method returns the correct value. 
	 * First, create two Questions and a ScoreComparator. 
	 * Next, upvote one Question, so the Score is different.
	 * Then, check if the compare method returns the correct value when
	 * Questions with different scores. 
	 * Methods tested: compare
	 * 
	 * @throws Exception
	 */
	public void testCompare() throws Exception {
		QuestionUpvoteComparator questionUpvoteComparator = new QuestionUpvoteComparator();
		Question question1 = new Question("TestContent1", "TestUserName1",
				"TestTitle1", null);
		Question question2 = new Question("TestContent2", "TestUserName2",
				"TestTitle2", null);
		
		question1.setUpvoteCount(1);

		assertEquals(-1, questionUpvoteComparator.compare(question1, question2));
		assertEquals(1, questionUpvoteComparator.compare(question2, question1));
		tearDown();
	}
}
