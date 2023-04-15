package com.pthore.service.posts.dto;

import java.util.List;

public class PostDto {

	private String _id;
	
	private String postTitle;
	
	private List<String> elementTypes;
	
	// This contains Text data and image download links.
	private List<String> postData;
	
	private Long imageCount;
	
	
	public PostDto() {
	}

	

	public Long getImageCount() {
		return imageCount;
	}

	public void setImageCount(Long imageCount) {
		this.imageCount = imageCount;
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

	public List<String> getElementTypes() {
		return elementTypes;
	}

	public void setElementTypes(List<String> elementTypes) {
		this.elementTypes = elementTypes;
	}

	public List<String> getPostData() {
		return postData;
	}

	public void setPostData(List<String> postData) {
		this.postData = postData;
	}
	
	

}
