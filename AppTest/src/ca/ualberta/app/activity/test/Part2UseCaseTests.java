package ca.ualberta.app.activity.test;

import java.util.ArrayList;

import android.graphics.Bitmap;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.CacheController;
import ca.ualberta.app.models.InputsListController;
import ca.ualberta.app.models.PushController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.InputsListModel;
import ca.ualberta.app.models.Reply;
import junit.framework.TestCase;

public class Part2UseCaseTests extends TestCase {
	public void testUserBrowse() {
		Question q = new Question(1,"A question", "userName", "A title", null);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(q);
		ArrayList<Question> qList = inputsListController.getQuestionList().getArrayList();
		assertTrue("User cannot browse question", qList.size() == 1);
	}

	public void testUserViewAnswer() {
		Question q = new Question(1,"A question", "userName", "A title", null);
		Answer answer = new Answer("A answer", "userName", null);
		InputsListController inputsListController = new InputsListController();
		q.addAnswer(answer);
		inputsListController.addQuestion(q);
		ArrayList<Answer> answerList = inputsListController.getAnswers(0);
		assertTrue("User cannot view answers", answerList.size() == 1);
	}

	public void testUserViewReply() {
		Question q = new Question(1,"A question", "userName", "A title", null);
		Reply reply = new Reply("A reply","userName");
		InputsListController inputsListController = new InputsListController();
		q.addReply(reply);
		inputsListController.addQuestion(q);
		ArrayList<Reply> replyList = inputsListController.getReplys(0);
		assertTrue("User cannot view answers", replyList.size() == 1);
	}

	public void testAuthorCreateQuestion() {
		Author author = new Author("userName");
		Question q = new Question(1,"A question", author.getUserName(), "A title", null);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(q);
		assertTrue("Author cannot Create Question", inputsListController.size() != 0);
	}

	public void testAuthorAnswerQuestion() {
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Answer answer = new Answer("A answer",author.getUserName(),null);
		question.addAnswer(answer);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		assertTrue("Author cannot answer question",
				inputsListController.getAnswerList(0).size() != 0);
	}

	public void testAuthorReplyQuestion() {
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Reply reply = new Reply("A reply", author.getUserName());
		question.addReply(reply);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		assertTrue("Author cannot answer question", inputsListController
				.getReplyList(0).size() != 0);
	}

	public void testAuthorAddPicture() {
		Author author = new Author("userName");
		Bitmap image = null;
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		image = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
		question.setImage(image);
		assertTrue("Author cannot add picture", question.hasImage());
	}

	public void testSysadminChangePicSize() {
		Bitmap image = Bitmap.createBitmap(20, 5, Bitmap.Config.ALPHA_8);
		assertTrue("Picture is larger than 64Kb", image.getByteCount() > 64);
	}

	public void testSortQuestionByPicture() {
		Author author = new Author("userName");
		Bitmap image1 = null;
		Bitmap image2 = Bitmap.createBitmap(5, 5, Bitmap.Config.ALPHA_8);
		Question question1 = new Question(1,"A question", author.getUserName(),
				"A title", image1);
		Question question2 = new Question(2,"A question", author.getUserName(),
				"A title", image2);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question1);
		inputsListController.addQuestion(question2);
		InputsListModel qList = inputsListController.getQuestionList().sortByPicture();
		assertTrue("Questions are not sort by picture", qList.getQuestion(0)
				.hasImage());
	}

	public void testSortQuestionByScore() {
		Author author = new Author("userName");
		Question question1 = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Question question2 = new Question(2,"A question", author.getUserName(),
				"A title", null);
		InputsListController inputsListController = new InputsListController();
		question1.upvote();
		inputsListController.addQuestion(question1);
		inputsListController.addQuestion(question2);
		InputsListModel qList = inputsListController.getQuestionList().sortByScore();
		assertTrue("Questions are not sort by Score", qList.getQuestion(0)
				.equals(question1));
	}

	public void testUpvoteQuestion() {
		Author author = new Author("userName");
		Question question1 = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Question question2 = new Question(2,"A question", author.getUserName(),
				"A title", null);
		InputsListController inputsListController = new InputsListController();
		question1.upvote();
		inputsListController.addQuestion(question1);
		inputsListController.addQuestion(question2);
		assertTrue("Questions are not upvoted", inputsListController.getQuestion(0)
				.getUpvoteCount() == 1);
	}

	public void testUpvoteAnswer() {
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Answer answer1 = new Answer("1st Answer", author.getUserName(), null);
		Answer answer2 = new Answer("2nd Answer", author.getUserName(), null);
		question.addAnswer(answer1);
		question.addAnswer(answer2);
		answer1.upvote();
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		inputsListController.getQuestion(0).addAnswer(answer1);
		inputsListController.getQuestion(0).addAnswer(answer2);
		assertTrue("Questions are not upvoted",
				inputsListController.getAnswers(0).get(0).getUpvoteCount() == 1);
	}

	public void testAnswerCounterOfAQuestion() {
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Answer answer1 = new Answer("1st Answer", author.getUserName(), null);
		Answer answer2 = new Answer("2nd Answer", author.getUserName(), null);
		question.addAnswer(answer1);
		question.addAnswer(answer2);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		assertTrue("Answer Counter is not working", inputsListController.getAnswers(0)
				.size() == 2);
	}

	public void testSearchQuestion() {
		String content = "A question";
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		InputsListModel resultList = inputsListController.searchQuestion(content);
		assertTrue("Search is not working well",
				resultList.getQuestion(0).getContent().equals(content));
	}

	public void testSearchAnswer() {
		String content = "A Answer";
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Answer answer = new Answer("A Answer", author.getUserName(), null);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		inputsListController.getQuestion(0).addAnswer(answer);
		InputsListModel resultList = inputsListController.searchAnswer(content);
		assertTrue("Search is not working well",
				resultList.getQuestion(0).getContent().equals(content));
	}

	public void testAuthorAskedQuestionCache() {
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		CacheController cacheController = new CacheController();
		cacheController.cacheQuestions(inputsListController.getQuestionList());
		// shut down Internet
		InputsListModel qList = cacheController.loadQuestions();
		assertTrue("Questions are not cached", qList.equals(inputsListController.getQuestionList()));
	}

	public void testReadQuestionAndAnswerCache() {
		Author author = new Author("userName");
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		Answer answer = new Answer("A Answer", author.getUserName(), null);
		inputsListController.getQuestion(0).addAnswer(answer);
		CacheController cacheController = new CacheController();
		cacheController.cacheQuestions(inputsListController.getQuestionList());
		// shut down Internet
		ArrayList<Answer> answerList = new ArrayList<Answer>();
		answerList.add(answer);
		InputsListModel qList = cacheController.loadQuestions();

		assertTrue("Questions are not cached", qList.equals(inputsListController.getQuestionList()));
		assertTrue("Answers are not cached", qList.getAnswerList(0).get(0)
				.equals(answerList.get(0)));
	}

	public void testFavouriteQuestions() {
		Author author = new Author("userName");
		Question question1 = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Question question2 = new Question(1,"A question", author.getUserName(),
				"A title", null);
		author.favorite.addQuestion(question1);
		author.favorite.addQuestion(question2);
		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question1);
		inputsListController.addQuestion(question2);
		CacheController cacheController = new CacheController();
		cacheController.cacheQuestions(inputsListController.getQuestionList());
		// shut down Internet
		InputsListModel qList = cacheController.loadFavouriteQuestion();
		assertTrue("Favourite Question is not cached", qList.getArrayList()
				.get(0).equals(question1));
	}

	public void testPushContent() {
		Author author = new Author("userName");
		// shut down Internet
		Question question = new Question(1,"A question", author.getUserName(),
				"A title", null);
		Answer answer = new Answer("A Answer", author.getUserName(),null);
		Reply reply = new Reply("A reply", author.getUserName());

		InputsListController inputsListController = new InputsListController();
		inputsListController.addQuestion(question);
		inputsListController.getQuestion(0).addAnswer(answer);
		inputsListController.getQuestion(0).addReply(reply);

		// resume Internet
		PushController pushController = new PushController();
		pushController.push(inputsListController.getQuestionList());
		// may need to test on a different phone
	}

//	public void testFreshestComments() {
//		
//	}

	public void testAuthorSetUserName() {
		Author author = new Author("userName");
		String userName = "newUserName";
		author.setUserName(userName);
		assertTrue("Author cannot set UserName",
				userName.equals(author.getUserName()));
	}
}
