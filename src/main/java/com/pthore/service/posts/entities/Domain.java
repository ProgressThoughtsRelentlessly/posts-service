package com.pthore.service.posts.entities;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")  
public class Domain {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,
			generator="domainSequenceGenerator")
	@SequenceGenerator(
			name = "domainSequenceGenerator", 
			sequenceName = "domain_sequence",
			initialValue = 0,
			allocationSize = 10
		)
	private String id;
	
	private String domainName;
	
	@Lob
	private String domainDescription;	
	
	// @JsonIgnore

	@ManyToMany( cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH }, mappedBy="followedDomains", fetch=FetchType.LAZY)
	private List<UserProfile> followedUsers;

	
	@ManyToMany( mappedBy="taggedDomains", cascade = CascadeType.MERGE, fetch=FetchType.LAZY)
	private List<UserPostMetadata> userPostMetadataList;
	
	
	
	public List<UserPostMetadata> getUserPostMetadataList() {
		return userPostMetadataList;
	}

	public void setUserPostMetadataList(List<UserPostMetadata> userPostMetadataList) {
		this.userPostMetadataList = userPostMetadataList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainDescription() {
		return domainDescription;
	}

	public void setDomainDescription(String domainDescription) {
		this.domainDescription = domainDescription;
	}

	public List<UserProfile> getFollowedUsers() {
		return followedUsers;
	}

	public void setFollowedUsers(List<UserProfile> followedUsers) {
		this.followedUsers = followedUsers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domainDescription, domainName, followedUsers, id, userPostMetadataList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Domain other = (Domain) obj;
		return Objects.equals(domainName, other.domainName);
	
	}
	
	
	
}
