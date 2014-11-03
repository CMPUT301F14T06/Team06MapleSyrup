//package ca.ualberta.app.activity.test;
//
//import java.util.ArrayList;
//
//import android.graphics.Bitmap;
//
//import ca.ualberta.app.models.Answer;
//import ca.ualberta.app.models.Author;
//import ca.ualberta.app.models.CacheController;
//import ca.ualberta.app.models.QuestionListController;
//import ca.ualberta.app.models.PushController;
//import ca.ualberta.app.models.Question;
//import ca.ualberta.app.models.QuestionList;
//import ca.ualberta.app.models.Reply;
//import junit.framework.TestCase;
//
//public class Part2UseCaseTests extends TestCase {
//	public void testUserBrowse() {
//		Question q = new Question("A question", "username", "A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(q);
//		ArrayList<Question> qList = inputsListController.getQuestionList().getArrayList();
//		assertTrue("User cannot browse question", qList.size() == 1);
//	}
//
//	public void testUserViewAnswer() {
//		Question q = new Question("A question", "username", "A title", null);
//		Answer answer = new Answer("A answer", "username", null);
//		QuestionListController inputsListController = new QuestionListController();
//		q.addAnswer(answer);
//		inputsListController.addQuestion(q);
//		ArrayList<Answer> answerList = inputsListController.getAnswers(0);
//		assertTrue("User cannot view answers", answerList.size() == 1);
//	}
//
//	public void testUserViewReply() {
//		Question q = new Question("A question", "username", "A title", null);
//		Reply reply = new Reply("A reply","username");
//		QuestionListController inputsListController = new QuestionListController();
//		q.addReply(reply);
//		inputsListController.addQuestion(q);
//		ArrayList<Reply> replyList = inputsListController.getReplys(0);
//		assertTrue("User cannot view answers", replyList.size() == 1);
//	}
//
//	public void testAuthorCreateQuestion() {
//		Author author = new Author("username");
//		Question q = new Question("A question", author.getUsername(), "A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(q);
//		assertTrue("Author cannot Create Question", inputsListController.size() != 0);
//	}
//
//	public void testAuthorAnswerQuestion() {
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		Answer answer = new Answer("A answer",author.getUsername(),null);
//		question.addAnswer(answer);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		assertTrue("Author cannot answer question",
//				inputsListController.getAnswerList(0).size() != 0);
//	}
//
//	public void testAuthorReplyQuestion() {
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		Reply reply = new Reply("A reply", author.getUsername());
//		question.addReply(reply);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		assertTrue("Author cannot answer question", inputsListController
//				.getReplyList(0).size() != 0);
//	}
//
//	public void testAuthorAddPicture() {
//		Author author = new Author("username");
//		Bitmap image = null;
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		image = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
//		question.setImage(image);
//		assertTrue("Author cannot add picture", question.hasImage());
//	}
//
//	public void testSysadminChangePicSize() {
//		Bitmap image = Bitmap.createBitmap(20, 5, Bitmap.Config.ALPHA_8);
//		assertTrue("Picture is larger than 64Kb", image.getByteCount() > 64);
//	}
//
//	public void testSortQuestionByPicture() {
//		Author author = new Author("username");
//		Bitmap image1 = null;
//		Bitmap image2 = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
//		Question question1 = new Question("A question", author.getUsername(),
//				"A title", image1);
//		Question question2 = new Question("A question", author.getUsername(),
//				"A title", image2);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question1);
//		inputsListController.addQuestion(question2);
//		QuestionList qList = inputsListController.getQuestionList().sortByPicture();
//		assertTrue("Questions are not sort by picture", qList.getQuestion(0)
//				.hasImage());
//	}
//
//	public void testSortQuestionByScore() {
//		Author author = new Author("username");
//		Question question1 = new Question("A question", author.getUsername(),
//				"A title", null);
//		Question question2 = new Question("A question", author.getUsername(),
//				"A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		question1.upvote();
//		inputsListController.addQuestion(question1);
//		inputsListController.addQuestion(question2);
//		QuestionList qList = inputsListController.getQuestionList().sortByScore();
//		assertTrue("Questions are not sort by Score", qList.getQuestion(0)
//				.equals(question1));
//	}
//
//	public void testUpvoteQuestion() {
//		Author author = new Author("username");
//		Question question1 = new Question("A question", author.getUsername(),
//				"A title", null);
//		Question question2 = new Question("A question", author.getUsername(),
//				"A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		question1.upvote();
//		inputsListController.addQuestion(question1);
//		inputsListController.addQuestion(question2);
//		assertTrue("Questions are not upvoted", inputsListController.getQuestion(0)
//				.getUpvoteCount() == 1);
//	}
//
//	public void testUpvoteAnswer() {
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		Answer answer1 = new Answer("1st Answer", author.getUsername(), null);
//		Answer answer2 = new Answer("2nd Answer", author.getUsername(), null);
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		answer1.upvote();
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		inputsListController.getQuestion(0).addAnswer(answer1);
//		inputsListController.getQuestion(0).addAnswer(answer2);
//		assertTrue("Questions are not upvoted",
//				inputsListController.getAnswers(0).get(0).getUpvoteCount() == 1);
//	}
//
//	public void testAnswerCounterOfAQuestion() {
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		Answer answer1 = new Answer("1st Answer", author.getUsername(), null);
//		Answer answer2 = new Answer("2nd Answer", author.getUsername(), null);
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		assertTrue("Answer Counter is not working", inputsListController.getAnswers(0)
//				.size() == 2);
//	}
//
//	public void testSearchQuestion() {
//		String content = "A question";
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		QuestionList resultList = inputsListController.searchQuestion(content);
//		assertTrue("Search is not working well",
//				resultList.getQuestion(0).getContent().equals(content));
//	}
//
//	public void testSearchAnswer() {
//		String content = "A Answer";
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		Answer answer = new Answer("A Answer", author.getUsername(), null);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		inputsListController.getQuestion(0).addAnswer(answer);
//		QuestionList resultList = inputsListController.searchAnswer(content);
//		assertTrue("Search is not working well",
//				resultList.getQuestion(0).getContent().equals(content));
//	}
//
//	public void testAuthorAskedQuestionCache() {
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(inputsListController.getQuestionList());
//		// shut down Internet
//		QuestionList qList = cacheController.loadQuestions();
//		assertTrue("Questions are not cached", qList.equals(inputsListController.getQuestionList()));
//	}
//
//	public void testReadQuestionAndAnswerCache() {
//		Author author = new Author("username");
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		Answer answer = new Answer("A Answer", author.getUsername(), null);
//		inputsListController.getQuestion(0).addAnswer(answer);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(inputsListController.getQuestionList());
//		// shut down Internet
//		ArrayList<Answer> answerList = new ArrayList<Answer>();
//		answerList.add(answer);
//		QuestionList qList = cacheController.loadQuestions();
//
//		assertTrue("Questions are not cached", qList.equals(inputsListController.getQuestionList()));
//		assertTrue("Answers are not cached", qList.getAnswerList(0).get(0)
//				.equals(answerList.get(0)));
//	}
//
//	public void testFavouriteQuestions() {
//		Author author = new Author("username");
//		Question question1 = new Question("A question", author.getUsername(),
//				"A title", null);
//		Question question2 = new Question("A question", author.getUsername(),
//				"A title", null);
//		author.favorite.addQuestion(question1);
//		author.favorite.addQuestion(question2);
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question1);
//		inputsListController.addQuestion(question2);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(inputsListController.getQuestionList());
//		// shut down Internet
//		QuestionList qList = cacheController.loadFavouriteQuestion();
//		assertTrue("Favourite Question is not cached", qList.getArrayList()
//				.get(0).equals(question1));
//	}
//
//	public void testPushContent() {
//		Author author = new Author("username");
//		// shut down Internet
//		Question question = new Question("A question", author.getUsername(),
//				"A title", null);
//		Answer answer = new Answer("A Answer", author.getUsername(),null);
//		Reply reply = new Reply("A reply", author.getUsername());
//
//		QuestionListController inputsListController = new QuestionListController();
//		inputsListController.addQuestion(question);
//		inputsListController.getQuestion(0).addAnswer(answer);
//		inputsListController.getQuestion(0).addReply(reply);
//
//		// resume Internet
//		PushController pushController = new PushController();
//		pushController.push(inputsListController.getQuestionList());
//		// may need to test on a different phone
//	}
//
////	public void testFreshestComments() {
////		
////	}
//
//	public void testAuthorSetUsername() {
//		Author author = new Author("username");
//		String username = "newUsername";
//		author.setUsername(username);
//		assertTrue("Author cannot set Username",
//				username.equals(author.getUsername()));
//	}
//}
