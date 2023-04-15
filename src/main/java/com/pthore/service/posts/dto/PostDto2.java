package com.pthore.service.posts.dto;

import java.util.List;

import com.pthore.service.posts.documents.PostComment;
import com.pthore.service.posts.documents.PostNode;

public class PostDto2 {
	
	private String _id;
	
	private String authorEmail;
	
	private String operation;
	
	private String nodeId;
	
	private String postTitle;
	
	private String postStatus; // 3 possible statuses: drafting, saved, created
	
	private List<PostNode> postNodes;

	private List<PostComment> postComments;

	
	
	@Override
	public String toString() {
		return "PostDto2 [_id=" + _id + ", authorEmail=" + authorEmail + ", operation=" + operation + ", nodeId="
				+ nodeId + ", postTitle=" + postTitle + ", postStatus=" + postStatus + ", postNodes=" + postNodes
				+ ", postComments=" + postComments + "]";
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}

	public List<PostNode> getPostNodes() {
		return postNodes;
	}

	public void setPostNodes(List<PostNode> postNodes) {
		this.postNodes = postNodes;
	}

	public List<PostComment> getPostComments() {
		return postComments;
	}

	public void setPostComments(List<PostComment> postComments) {
		this.postComments = postComments;
	}


}
