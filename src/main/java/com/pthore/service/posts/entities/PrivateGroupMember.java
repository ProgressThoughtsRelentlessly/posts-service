package com.pthore.service.posts.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
@Entity
public class PrivateGroupMember {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="GroupMemberSequenceGenerator")
	@SequenceGenerator(
			name = "GroupMemberSequenceGenerator", 
			sequenceName = "groupMember_sequence",
			initialValue = 1,
			allocationSize = 1
		)	
	private Long id;
	
	private String email;
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private UserProfile user;
	
	@CreationTimestamp
	private LocalDate groupJoinDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public LocalDate getGroupJoinDate() {
		return groupJoinDate;
	}

	public void setGroupJoinDate(LocalDate groupJoinDate) {
		this.groupJoinDate = groupJoinDate;
	}
	
	
	
	
	
}
