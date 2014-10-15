package ca.ualberta.app.models;

public class Reply extends InputsModel{
	String replyContent;
	
	public Reply (String reply, String authorLoginName){
		super(reply, authorLoginName);
		this.replyContent = reply;
	}
	
	public String getReply() {
		return this.replyContent;
	}
}
