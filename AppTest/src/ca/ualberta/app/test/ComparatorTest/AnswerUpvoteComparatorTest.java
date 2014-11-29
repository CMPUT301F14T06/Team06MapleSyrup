//package ca.ualberta.app.test.ComparatorTest;
//
//import android.test.ActivityInstrumentationTestCase2;
//import ca.ualberta.app.activity.MyFavoriteActivity;
//import ca.ualberta.app.comparator.AnswerUpvoteComparator;
//import ca.ualberta.app.models.Answer;
//import ca.ualberta.app.models.Question;
//
//public class AnswerUpvoteComparatorTest extends
//		ActivityInstrumentationTestCase2<MyFavoriteActivity> {
//
//	/**
//	 * Constructor
//	 */
//	public AnswerUpvoteComparatorTest() {
//		super(MyFavoriteActivity.class);
//	}
//
//	/**
//	 * Test whether the compare method returns the correct value. 
//	 * First, create two Questions , two Answers and a AnswerUpvoteComparator. 
//	 * Next, upvote one Answer.
//	 * Then, check if the compare method returns the correct value when
//	 * Questions with different number of upvotes. 
//	 * Methods tested: compare
//	 * 
//	 * @throws Exception
//	 */
//	public void testCompare() throws Exception {
//		AnswerUpvoteComparator answerUpvoteComparator = new AnswerUpvoteComparator();
//		Question question1 = new Question("TestContent1", "TestUserName1",
//				"TestTitle1", null);
//		Question question2 = new Question("TestContent2", "TestUserName2",
//				"TestTitle2", null);
//		Answer answer1 = new Answer("TestAnswerContent1", "TestUserName1", null);
//		Answer answer2 = new Answer("TestAnswerContent2", "TestUserName2", null);
//		
//		answer1.setUpvoteCount(1);
//		question1.addAnswer(answer1);
//		question2.addAnswer(answer2);
//
//		assertEquals(-1, answerUpvoteComparator.compare(question1, question2));
//		assertEquals(1, answerUpvoteComparator.compare(question2, question1));
//		tearDown();
//	}
//}
