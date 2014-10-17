package ca.ualberta.app.activity.test;

import java.util.ArrayList;

import android.graphics.Bitmap;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.InputsListModel;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import junit.framework.TestCase;

public class Part2UseCaseTests extends TestCase {
//	public void testUserBrowse() {
//		User user = new User();
//		Bitmap image = null;
//		Question q = new Question("A question", "A author", "A title",
//				image);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(q);
//		ArrayList<Question> qList = questionList.getArrayList();
//		assertTrue("User cannot browse question", qList.size() == 1);
//	}
//
//	public void testUserViewAnswer() {
//		Bitmap image = null;
//		Question q = new Question("A question", "A author", "A title",
//				image);
//		Answer answer = new Answer("A answer", "A author", image);
//		InputsListModel questionList = new InputsListModel();
//		q.addAnswer(answer);
//		questionList.addQuestion(q);
//		ArrayList<Answer> answerList = questionList.getAnswers(0);
//		assertTrue("User cannot view answers", answerList.size() == 1);
//	}
//
//	public void testUserViewReply() {
//		User user = new User();
//		Bitmap image = null;
//		Question q = new Question("A question", "A author", "A title",
//				image);
//		Reply reply = new Reply("A reply", "A author");
//		InputsListModel questionList = new InputsListModel();
//		q.addReply(reply);
//		questionList.addQuestion(q);
//		ArrayList<Answer> replyList = questionList.getAnswers(0);
//		assertTrue("User cannot view answers", replyList.size() == 1);
//	}
//
//	public void testAuthorCreateQuestion() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		assertTrue("Author cannot Create Question", questionList.size() != 0);
//	}
//
//	public void testAuthorAnswerQuestion() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		Answer answer = new Answer("A Answer", author.getAuthorName(),
//				image);
//		question.addAnswer(answer);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		assertTrue("Author cannot answer question",
//				questionList.getAnswerList(0).size() != 0);
//	}
//
//	public void testAuthorReplyQuestion() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		Reply reply = new Reply("A reply", "A author");
//		question.addReply(reply);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		assertTrue("Author cannot answer question", questionList
//				.getReplyList(0).size() != 0);
//	}
//
//	public void testAuthorAddPicture() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		image = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
//		question.setImage(image);
//		assertTrue("Author cannot add picture",
//				question.hasImage());
//	}
//
//	public void testSysadminChangePicSize() {
//		Sysadmin sysadmin = new Sysadmin("loginName", 1);
//		Author author = new Author("loginName2", 2);
//		Bitmap image = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		assertTrue("Picture is larger than 64Kb",
//				image.getByteCount() > 64);
//	}
//
//	public void testSortQuestionByPicture() {
//		Author author = new Author("loginName", 1);
//		Bitmap image1 = null;
//		Bitmap image = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
//		Question question1 = new Question("1st question",
//				author.getAuthorName(), "A title", image);
//		Question question2 = new Question("2nd question",
//				author.getAuthorName(), "A title", image1);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		InputsListModel qList = questionList.getArrayList().sortByPicture();
//		assertTrue("Questions are not sort by picture", qList.getQuestion(0)
//				.hasImage());
//	}
//
//	public void testSortQuestionByScore() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question1 = new Question("1st question",
//				author.getAuthorName(), "A title", image);
//		Question question2 = new Question("2nd question",
//				author.getAuthorName(), "A title", image);
//		InputsListModel questionList = new InputsListModel();
//		question1.upvote();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		InputsListModel qList = questionList.getArrayList().sortByScore();
//		assertTrue("Questions are not sort by Score", qList.getQuestion(0)
//				.equals(question1));
//	}
//
//	public void testUpvoteQuestion() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question1 = new Question("1st question",
//				author.getAuthorName(), "A title", image);
//		Question question2 = new Question("2nd question",
//				author.getAuthorName(), "A title", image);
//		InputsListModel questionList = new InputsListModel();
//		question1.upvote();
//		question2.downvote();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		assertTrue("Questions are not upvoted", questionList.getQuestion(0)
//				.getUpvoteCount() == 1);
//		assertTrue("Questions are not upvoted", questionList.getQuestion(1)
//				.getUpvoteCount() == -1);
//	}
//
//	public void testUpvoteAnswer() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		Answer answer1 = new Answer("1st Answer", author.getAuthorName(),
//				image);
//		Answer answer2 = new Answer("2nd Answer", author.getAuthorName(),
//				image);
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		answer1.upvote();
//		answer2.downvote();
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		questionList.getQuestion(0).addAnswer(answer1);
//		questionList.getQuestion(0).addAnswer(answer2);
//		assertTrue("Questions are not upvoted",
//				questionList.getAnswers(0).get(0).getUpvoteCount() == 1);
//		assertTrue("Questions are not upvoted",
//				questionList.getAnswers(0).get(1).getUpvoteCount() == -1);
//	}
//
//	public void testAnswerCounterOfAQuestion() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		Answer answer1 = new Answer("1st Answer", author.getAuthorName(),
//				image);
//		Answer answer2 = new Answer("2nd Answer", author.getAuthorName(),
//				image);
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		assertTrue("Answer Counter is not working", questionList.getAnswers(0)
//				.size() == 2);
//	}
//
//	public void testSearchQuestion() {
//		String content = "A question";
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		Question result = questionList.search(content);
//		assertTrue("Search is not working well",
//				result.getContent().equals(content));
//	}
//
//	public void testSearchAnswer() {
//		String content = "A Answer";
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		Answer answer = new Answer("A Answer", author.getAuthorName(),
//				image);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		questionList.getQuestion(0).addAnswer(answer);
//		Answer result = questionList.getAnswers(0).search(content);
//		assertTrue("Search is not working well",
//				result.getContent().equals(content));
//	}
//
//	public void testAuthorAskedQuestionCache() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(questionList);
//		// shut down Internet
//		InputsListModel qList = cacheController.loadQuestions();
//		assertTrue("Questions are not cached", qList.equals(questionList));
//	}
//
//	public void testReadQuestionAndAnswerCache() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question = new Question("A question", author.getAuthorName(),
//				"A title", image);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		Answer answer = new Answer("A Answer", author.getAuthorName(),
//				image);
//		questionList.getQuestion(0).addAnswer(answer);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(questionList);
//		// shut down Internet
//		ArrayList<Answer> answerList = new ArrayList<Answer>();
//		answerList.add(answer);
//		InputsListModel qList = cacheController.loadQuestions();
//
//		assertTrue("Questions are not cached", qList.equals(questionList));
//		assertTrue("Answers are not cached",
//				qList.getAnswerList(0).get(0).equals(answerList.get(0)));
//	}
//
//	public void testFavouriteQuestions() {
//		Author author = new Author("loginName", 1);
//		Bitmap image = null;
//		Question question1 = new Question("Favoutive question",
//				author.getAuthorName(), "A title", image);
//		Question question2 = new Question("normal question",
//				author.getAuthorName(), "A title", image);
//		author.favorite.addQuestion(question1);
//		author.favorite.addQuestion(question2);
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(questionList);
//		// shut down Internet
//		InputsListModel qList = cacheController.loadFavouriteQuestion();
//		assertTrue("Favourite Question is not cached", qList.getArrayList()
//				.get(0).equals(question1));
//	}
//
//	public void testPushContent() {
//		Author author = new Author("loginName", 1);
//		// shut down Internet
//		Bitmap image = null;
//		Question question = new Question("A question", author.getLoginName(),
//				"A title", image);
//		Answer answer = new Answer("A Answer", author.getLoginName(), image);
//		Reply reply = new Reply("A reply", author.getLoginName());
//
//		InputsListModel questionList = new InputsListModel();
//		questionList.addQuestion(question);
//		questionList.getQuestion(0).addAnswer(answer);
//		questionList.getQuestion(0).addReply(reply);
//
//		// resume Internet
//		PushController pushController = new PushController();
//		pushController.push(questionList);
//		// may need to test on a different phone
//	}
//
//	public void testFreshestComments() {
//
//	}
//
//	public void testAuthorSetUserName() {
//		Author author = new Author("loginName", 1);
//		String userName = "newUserName";
//		author.setName(userName);
//		assertTrue("Author cannot set UserName",
//				userName.equals(author.getAuthorName()));
//	}
}
