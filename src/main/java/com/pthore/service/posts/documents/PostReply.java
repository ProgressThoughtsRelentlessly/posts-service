package com.pthore.service.posts.documents;

public class PostReply {
	
	private String replierEmail;
	private String reply;
	
	
	
	public PostReply() {
	}
	
	
	public PostReply(String replierEmail, String reply) {
		super();
		this.replierEmail = replierEmail;
		this.reply = reply;
	}
	
	public String getReplierEmail() {
		return replierEmail;
	}
	public void setReplierEmail(String replierEmail) {
		this.replierEmail = replierEmail;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	
	
	
	
	
}
