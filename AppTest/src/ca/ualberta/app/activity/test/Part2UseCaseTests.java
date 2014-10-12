package ca.ualberta.app.activity.test;

import java.util.ArrayList;

import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.AnswerList;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionList;
import ca.ualberta.app.models.Reply;
import ca.ualberta.app.models.User;
import junit.framework.TestCase;

public class Part2UseCaseTests extends TestCase {
	public void testUserBrowse(){
		User user = new User();
		Question q = new Question("A question", "A author", "A title");
		QuestionList questionlist = new QuestionList();
		questionlist.addQuestion(q);
		ArrayList<Question> qList = user.getQuestionList();
		assertTrue("User cannot browse question",qList.size() == 1);
	}
	
	public void testUserViewAnswer(){
		User user = new User();
		Question question = new Question("A question", "A author", "A title");
		Answer answer = new Answer("A answer", "A author");
		QuestionList questionlist = new QuestionList();
		question.addAnswer(answer);
		questionlist.addQuestion(question);
		Question q = user.getQuestion(0);
		ArrayList<Answer> answerList = q.getAnswerList();
		assertTrue("User cannot view answers", answerList.size() == 1);
	}
	
	public void testUserViewReply(){
		User user = new User();
		Question question = new Question("A question", "A author", "A title");
		Reply reply = new Reply("A reply");
		QuestionList questionlist = new QuestionList();
		question.addReply(reply);
		questionlist.addQuestion(question);
		Question q = user.getQuestion(0);
		ArrayList<Answer> replyList = q.getReplyList();
		assertTrue("User cannot view answers", replyList.size() == 1);
	}
	
	public void testAuthorCreateQuestion(){
		Author author = new Author("loginName", 1);
		Question question = new Question("A question", author.getAuthorName(), "A title");
		assertTrue("User cannot Create Question", question.equals(author.getQuestion("A title")));
	}
	
}
