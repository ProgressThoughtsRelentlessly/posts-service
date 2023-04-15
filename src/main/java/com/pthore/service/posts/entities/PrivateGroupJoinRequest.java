package com.pthore.service.posts.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class PrivateGroupJoinRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="PrivateGroupJoinRequestSequenceGenerator")
	@SequenceGenerator(name = "PrivateGroupJoinRequestSequenceGenerator", sequenceName = "private_group_join_request_sequence")
	private Long id;
	
	private String requestStatus; // requested | accepted | rejected
	
	private String joinerEmail;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private UserProfile groupOwner;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private UserProfile joiner;

	
	public String getJoinerEmail() {
		return joinerEmail;
	}

	public void setJoinerEmail(String joinerEmail) {
		this.joinerEmail = joinerEmail;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public UserProfile getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(UserProfile groupOwner) {
		this.groupOwner = groupOwner;
	}

	public UserProfile getJoiner() {
		return joiner;
	}

	public void setJoiner(UserProfile joiner) {
		this.joiner = joiner;
	}
	
}
