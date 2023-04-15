package com.pthore.service.posts.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PthoreGroup {
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "group_sequence_generator")
	@SequenceGenerator( 
			name = "group_sequence_generator", 
			initialValue = 100, 
			allocationSize = 5, 
			sequenceName = "user_group_sequence")
	private Long id;
	
	private String groupName;
	
	private boolean isPrivate;
	
	@CreationTimestamp
	private LocalDate groupCreationDate;
	
	@UpdateTimestamp
	private LocalDate lastModifiedDate;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY) // mappedBy = "followedGroups", 
	List<UserProfile> groupMembers;
	
	@ElementCollection(fetch = FetchType.LAZY)
	List<String> groupOwnersEmails;
	
	
	public List<String> getGroupOwnersEmails() {
		return groupOwnersEmails;
	}

	public void setGroupOwnersEmails(List<String> groupOwnersEmails) {
		this.groupOwnersEmails = groupOwnersEmails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public LocalDate getGroupCreationDate() {
		return groupCreationDate;
	}

	public void setGroupCreationDate(LocalDate groupCreationDate) {
		this.groupCreationDate = groupCreationDate;
	}

	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public List<UserProfile> getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(List<UserProfile> groupMembers) {
		this.groupMembers = groupMembers;
	}

	
}
