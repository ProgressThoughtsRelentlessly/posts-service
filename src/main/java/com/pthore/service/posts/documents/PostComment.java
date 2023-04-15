package com.pthore.service.posts.documents;

import java.util.List;
import java.util.Map;

public class PostComment {
	
	private String commentId;
	private String email;
	private String comment;
	
	// key is the replier's email address, value is his comment. 
	private List<PostReply> commentReplies;
	
	public PostComment() {
	}

	@Override
	public String toString() {
		return "PostComment [commentId=" + commentId + ", email=" + email + ", comment=" + comment + ", commentReplies="
				+ commentReplies + "]";
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<PostReply> getCommentReplies() {
		return commentReplies;
	}

	public void setCommentReplies(List<PostReply> commentReplies) {
		this.commentReplies = commentReplies;
	}
}
