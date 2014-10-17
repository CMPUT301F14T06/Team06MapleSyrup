package ca.ualberta.app.activity.test;

import java.util.ArrayList;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.InputsListModel;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import junit.framework.TestCase;

public class Part2UseCaseTests extends TestCase {
//	public void testUserBrowse(){
//		User user = new User();
//		Question q = new Question("A question", "A author", "A title");
//		QuestionList questionlist = new QuestionList();
//		questionlist.addQuestion(q);
//		ArrayList<Question> qList = user.getQuestionList();
//		assertTrue("User cannot browse question",qList.size() == 1);
//	}
//	
//	public void testUserViewAnswer(){
//		User user = new User();
//		Question question = new Question("A question", "A author", "A title");
//		Answer answer = new Answer("A answer", "A author");
//		QuestionList questionlist = new QuestionList();
//		question.addAnswer(answer);
//		questionlist.addQuestion(question);
//		Question q = user.getQuestion(0);
//		ArrayList<Answer> answerList = q.getAnswerList();
//		assertTrue("User cannot view answers", answerList.size() == 1);
//	}
//	
//	public void testUserViewReply(){
//		User user = new User();
//		Question question = new Question("A question", "A author", "A title");
//		Reply reply = new Reply("A reply");
//		QuestionList questionlist = new QuestionList();
//		question.addReply(reply);
//		questionlist.addQuestion(question);
//		Question q = user.getQuestion(0);
//		ArrayList<Answer> replyList = q.getReplyList();
//		assertTrue("User cannot view answers", replyList.size() == 1);
//	}
//	
//	public void testAuthorCreateQuestion(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		assertTrue("Author cannot Create Question", question.equals(author.getQuestion("A title")));
//	}
//	
//	public void testAuthorAnswerQuestion(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		Answer answer = new Answer("A Answer", author.getAuthorName());
//		question.addAnswer(answer);
//		ArrayList<Answer> answerList = question.getAnswerList();
//		assertTrue("Author cannot answer question", answerList.contains(answer));
//	}
//	
//	public void testAuthorReplyQuestion(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		Reply reply = new Reply("A reply");
//		question.addReply(reply);
//		ArrayList<Reply> replyList = question.getReplyList();
//		assertTrue("Author cannot reply question", replyList.contains(reply));
//	}
//	
//	public void testAuthorAddPicture(){
//		Author author = new Author("loginName", 1);
//		Picture pic = new Picture(Bitmap picture);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		question.addPicture(pic);
//		assertTrue("Author cannot add picture", question.getPicture().equals(pic));
//	}
//	
//	public void testSysadminChangePicSize(){
//		Sysadmin sysadmin = new Sysadmin("loginName", 1);
//		Author author = new Author("loginName2", 2);
//		Picture pic = new Picture(Bitmap picture);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		question.addPicture(pic);
//		assertTrue("Picture is larger than 64Kb", pic.size() > 64); 
//	}
//	
//	public void testSortQuestionByPicture(){
//		Author author = new Author("loginName", 1);
//		Picture pic = new Picture(Bitmap picture);
//		Question question1 = new Question("1st question", author.getAuthorName(), "A title");
//		Question question2 = new Question("2nd question", author.getAuthorName(), "A title");
//		question1.addPicture(pic);
//		QuestionList questionList = new QuestionList();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		QuestionList qList = questionList.getArrayList().sortByPicture();
//		assertTrue("Questions are not sort by picture",qList.getQuestion(0).contains(pic));
//	}
//	
//	public void testSortQuestionByScore(){
//		Author author = new Author("loginName", 1);
//		Question question1 = new Question("1st question", author.getAuthorName(), "A title");
//		Question question2 = new Question("2nd question", author.getAuthorName(), "A title");
//		QuestionList questionList = new QuestionList();
//		question1.upvote();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		QuestionList qList = questionList.getArrayList().sortByScore();
//		assertTrue("Questions are not sort by Score",qList.getQuestion(0).equals(question1));
//	}
//	
//	public void testUpvoteQuestion(){
//		Author author = new Author("loginName", 1);
//		Question question1 = new Question("1st question", author.getAuthorName(), "A title");
//		Question question2 = new Question("2nd question", author.getAuthorName(), "A title");
//		QuestionList questionList = new QuestionList();
//		question1.upvote();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		QuestionList qList = questionList.getArrayList().sortByScore();
//		assertTrue("Questions are not upvoted",qList.getQuestion(0).equals(question1));
//	}
//	
//	public void testUpvoteAnswer(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		Answer answer1 = new Answer("1st Answer", author.getAuthorName());
//		Answer answer2 = new Answer("2nd Answer", author.getAuthorName());
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		answer2.upvote();
//		AnswerList answerList = question.getAnswerList().sortByScore();
//		assertTrue("Answers are not upvoted",answerList.getAnswer(0).equals(answer2));
//	}
//	
//	public void testAnswerCounterOfAQuestion(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		Answer answer1 = new Answer("1st Answer", author.getAuthorName());
//		Answer answer2 = new Answer("2nd Answer", author.getAuthorName());
//		question.addAnswer(answer1);
//		question.addAnswer(answer2);
//		assertTrue("Answer Counter is not working",question.getAnswerList().size() == 2);
//	}
//	
//	public void testSearchQuestion(){
//		String content = "A question";
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		QuestionList questionList = new QuestionList();
//		questionList.addQuestion(question);
//		Question result = questionList.search(content);
//		assertTrue("Search is not working well",result.getContent().equals(content));
//	}
//	
//	public void testSearchAnswer(){
//		String content = "A Answer";
//		Author author = new Author("loginName", 1);
//		Answer answer = new Answer("A Answer", author.getAuthorName());
//		AnswerList answerList = new AnswerList();
//		answerList.addAnswer(answer);
//		Answer result = answerList.search(content);
//		assertTrue("Search is not working well",result.getContent().equals(content));
//	}
//	
//	public void testAuthorAskedQuestionCache(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		QuestionList questionList = new QuestionList();
//		questionList.addQuestion(question);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(questionList);
//		// shut down Internet
//		QuestionList qList = cacheController.loadQuestions();
//		assertTrue("Questions are not cached",qList.equals(questionList));
//	}
//	
//	public void testReadQuestionAndAnswerCache(){
//		Author author = new Author("loginName", 1);
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		QuestionList questionList = new QuestionList();
//		questionList.addQuestion(question);		
//		Answer answer = new Answer("A Answer", author.getAuthorName());
//		AnswerList answerList = new AnswerList();
//		answerList.addAnswer(answer);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(questionList);
//		cacheController.cacheAnswers(answerList);
//		// shut down Internet
//		QuestionList qList = cacheController.loadQuestions();
//		AnswerList aList = cacheController.loadAnswers();
//		assertTrue("Questions are not cached",qList.equals(questionList));
//		assertTrue("Answers are not cached",qList.equals(questionList));
//	}
//	
//	public void testFavouriteQuestions(){
//		Author author = new Author("loginName", 1);
//		Question question1 = new Question("Favoutive question", author.getAuthorName(), "A title");
//		Question question2 = new Question("normal question", author.getAuthorName(), "A title");
//		question1.setFavourite();
//		QuestionList questionList = new QuestionList();
//		questionList.addQuestion(question1);
//		questionList.addQuestion(question2);
//		CacheController cacheController = new CacheController();
//		cacheController.cacheQuestions(questionList);
//		// shut down Internet
//		QuestionList qList = cacheController.loadFavouriteQuestion();
//		assertTrue("Favourite Question is not cached",qList.getArrayList().getQuestion(0).equals(question1));
//	}
//	
//	public void testPushContent(){
//		Author author = new Author("loginName", 1);
//		//shut down Internet
//		Question question = new Question("A question", author.getAuthorName(), "A title");
//		Answer answer = new Answer("A Answer", author.getAuthorName());
//		Reply reply = new Reply("A reply");
//		
//		QuestionList questionList = new QuestionList();
//		AnswerList answerList = new AnswerList();
//		ReplyList replyList = new ReplyList();
//		
//		questionList.addQuestion(question);
//		answerList.addAnswer(answer);
//		replyList.addReply(reply);
//		
//		//resume Internet
//		PushController pushController = new PushController();
//		pushController.push(questionList);
//		pushController.push(answerList);
//		pushController.push(replyList);
//		//may need to test on a different phone
//	}
//	
//	public void testFreshestComments(){
//		
//	}
//	
//	public void testAuthorSetUserName(){
//		Author author = new Author("loginName", 1);
//		String userName = "newUserName";
//		author.setUserName(userName);
//		assertTrue("Author cannot set UserName", userName.equals(author.getAuthorName()));
//	}
}
