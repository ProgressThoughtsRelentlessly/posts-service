package com.pthore.service.posts.documents;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
/*
schema:
db.posts.<document>
	{
		_id: 
		postTitle:
		status:
		postNodes: [
			 {	
			 	nodeId:
				sectionIndex:
				type:
				textData: 
				desktopImages: []
				mobileImages: []
			}
		],
		postComments: [
			{
				commentId:
				email:
				comment:
				replies: [{
					email:
					reply:
				}, 
				.... ]
			}
		]
	}


*/
@Document(value="posts")
public class Post2 {
	
	private String _id;
	
	@Field(value="authorEmail")
	private String authorEmail;
	
	@Field(value="postTitle")
	private String postTitle;
	
	@Field(value="postStatus")
	private String postStatus; // 3 possible statuses: drafting, saved, created
	
	@Field(value="postNodes")
	private List<PostNode> postNodes;
	
	@Field(value="postComments")
	private List<PostComment> postComments;

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
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
