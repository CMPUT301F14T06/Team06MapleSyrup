package ca.ualberta.app.models;

public class Reply {
	String replyContent;
	
	public Reply (String reply){
		this.replyContent = reply;
	}
	
	public String getReply() {
		return this.replyContent;
	}
}
