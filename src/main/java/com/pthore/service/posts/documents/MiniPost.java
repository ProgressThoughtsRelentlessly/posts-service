package com.pthore.service.posts.documents;

import java.time.LocalDate;
import java.util.Map;

public class MiniPost {
	
	private String postId;
	private byte[] thumbnail;
	private String authorName;
	private String authorProfileLink;
	private LocalDate postCreationDate;
	private String miniPostData;
	private String postTitle;
	private Map<String, String> taggedDomainsWithLinks;
	
	public MiniPost() {
	}
	
	
	
	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public Map<String, String> getTaggedDomainsWithLinks() {
		return taggedDomainsWithLinks;
	}
	public void setTaggedDomainsWithLinks(Map<String, String> taggedDomainsWithLinks) {
		this.taggedDomainsWithLinks = taggedDomainsWithLinks;
	}
	public byte[] getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorProfileLink() {
		return authorProfileLink;
	}
	public void setAuthorProfileLink(String authorProfileLink) {
		this.authorProfileLink = authorProfileLink;
	}
	public LocalDate getPostCreationDate() {
		return postCreationDate;
	}
	public void setPostCreationDate(LocalDate postCreationDate) {
		this.postCreationDate = postCreationDate;
	}
	public String getMiniPostData() {
		return miniPostData;
	}
	public void setMiniPostData(String miniPostData) {
		this.miniPostData = miniPostData;
	}
	
	
	
	
	
}
