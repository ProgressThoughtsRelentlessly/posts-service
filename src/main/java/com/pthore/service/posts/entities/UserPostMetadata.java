package com.pthore.service.posts.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class UserPostMetadata {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="userPostMetaDataGenerator")
	@SequenceGenerator(
		name = "userPostMetaDataGenerator", 
		sequenceName = "user_post_metadata_sequence",
		initialValue = 1,
		allocationSize = 1
	)
	private Long id;
	
	private String authorEmail;
	
	private Long upvotes;
	
	@Column(nullable=false)
	private String postId;
	
	@Column(nullable=false)
	@Lob
	private String postTitle;
	
	@CreationTimestamp
	@Column(nullable=false)
	private LocalDate postCreationDate;
	
	@UpdateTimestamp
	private LocalDate postLastModifiedDate;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH } , optional = false)
	private UserProfile userProfile;
	
	@ManyToMany(fetch= FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="post_tagged_domains")
	private List<Domain> taggedDomains;
	
	public UserPostMetadata() {
	}

	
	public String getAuthorEmail() {
		return authorEmail;
	}
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	public List<Domain> getTaggedDomains() {
		return taggedDomains;
	}
	public void setTaggedDomains(List<Domain> taggedDomains) {
		this.taggedDomains = taggedDomains;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUpvotes() {
		return upvotes;
	}

	public void setUpvotes(Long upvotes) {
		this.upvotes = upvotes;
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

	public LocalDate getPostCreationDate() {
		return postCreationDate;
	}

	public void setPostCreationDate(LocalDate postCreationDate) {
		this.postCreationDate = postCreationDate;
	}

	public LocalDate getPostLastModifiedDate() {
		return postLastModifiedDate;
	}

	public void setPostLastModifiedDate(LocalDate postLastModifiedDate) {
		this.postLastModifiedDate = postLastModifiedDate;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	
	
	
	
	
}
