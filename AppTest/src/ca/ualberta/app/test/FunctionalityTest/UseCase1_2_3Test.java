//package ca.ualberta.app.test.FunctionalityTest;
//
//import java.util.ArrayList;
//
//import ca.ualberta.app.controller.QuestionListController;
//import ca.ualberta.app.models.Answer;
//import ca.ualberta.app.models.Question;
//import ca.ualberta.app.models.Reply;
//import junit.framework.TestCase;
////Test use case1.2.3
//public class UseCase1_2_3Test extends TestCase {
//	String questionString = "A Question";
//	String userName = "userName";
//	String titleString = "title";
//	String imageByteArray = null;
//	Question q1 = new Question(questionString, userName, titleString,
//			imageByteArray);
//	
//	
//	//Test case for Use Case 1: User browses questions
//	public void testUseCase1() {
//		QuestionListController qlc = new QuestionListController();
//		ArrayList<Question> emptyList = qlc.getQuestionList().getArrayList();
//		assertTrue("User can browse question", emptyList.size() == 0);
//		
//		qlc.addQuestion(q1);
//		ArrayList<Question> qList = qlc.getQuestionList().getArrayList();
//		assertTrue("User cannot browse question", qList.size() == 1);
//	}
//	
//	
//	//Test case for Use Case 2: User views a question and its answers
//	public void testUseCase2() {
//		String AnswerString = "A answer";
//		Answer a = new Answer(AnswerString, userName, imageByteArray);
//		QuestionListController qlc = new QuestionListController();
//		q1.addAnswer(a);
// 	   qlc.addQuestion(q1);
// 	    ArrayList<Answer> aList = qlc.getAnswers(0);
//		assertTrue("User cannot view answers", aList.size() == 1);
//		}
//	
//	//Test case for Use Case 3: User views replies to an answer or question
//	public void testUseCase3() {
//		String ReplyString = "A Reply";
//		Reply r = new Reply(ReplyString, userName);
//		QuestionListController qlc = new QuestionListController();
//		q1.addReply(r);
//		qlc.addQuestion(q1);
//		ArrayList<Reply> rList = qlc.getReplys(0);
//		assertTrue("User cannot view answers", rList.size() == 1);
//	}
//}
//
