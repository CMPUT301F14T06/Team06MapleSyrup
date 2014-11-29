//package ca.ualberta.app.test.ComparatorTest;
//
//import java.io.ByteArrayOutputStream;
//
//import ca.ualberta.app.activity.MyFavoriteActivity;
//import ca.ualberta.app.comparator.PictureComparator;
//import ca.ualberta.app.models.Question;
//import android.graphics.Bitmap;
//import android.test.ActivityInstrumentationTestCase2;
//import android.util.Base64;
//
///**
// * JUnit test cases for PictureComparator.
// */
//public class PictureComparatorTest extends
//		ActivityInstrumentationTestCase2<MyFavoriteActivity> {
//
//	/**
//	 * Constructor
//	 */
//	public PictureComparatorTest() {
//		super(MyFavoriteActivity.class);
//	}
//
//	/**
//	 * Test whether the compare method returns the correct value. 
//	 * First, create two Questions and a PictureComparator. Then, check if the
//	 * compare method returns the correct value when Questions with or without a
//	 * picture are passed in.
//	 * Methods tested: compare
//	 * 
//	 * @throws Exception
//	 */
//	public void testCompare() throws Exception {
//		PictureComparator pictureComparator = new PictureComparator();
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		Bitmap pic = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
//		pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
//		byte[] byteArray = stream.toByteArray();
//		byte[] imageByteArray = Base64.encode(byteArray, 1);
//		String imageString = new String(imageByteArray, "UTF-8");
//		
//		Question question1 = new Question("TestContent1", "TestUserName1",
//				"TestTitle1", null);
//		Question question2 = new Question("TestContent2", "TestUserName2",
//				"TestTitle2", null);
//		Question question3 = new Question("TestContent3", "TestUserName3",
//				"TestTitle3", imageString);
//		Question question4 = new Question("TestContent4", "TestUserName4",
//				"TestTitle4", imageString);
//		
//		assertEquals(0, pictureComparator.compare(question1, question2));
//		assertEquals(0, pictureComparator.compare(question3, question4));
//		assertEquals(-1, pictureComparator.compare(question3, question1));
//		assertEquals(1, pictureComparator.compare(question2, question4));
//		tearDown();
//	}
//}
