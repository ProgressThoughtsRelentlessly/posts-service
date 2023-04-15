package com.pthore.service.posts.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="userProfileSequenceGenerator")
	@SequenceGenerator(
			name = "userProfileSequenceGenerator", 
			sequenceName = "user_profile_sequence",
			initialValue = 1,
			allocationSize = 1
		)
	private Long id;
	
	private String profilePicture;
	
	@Column(columnDefinition="bigint default 0")
	private Long learningStreak;
	
	@Column(nullable = false)
	private String firstname;
	
	@Column(nullable = true)
	private String lastname;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = true)
	private String mobile;
	
	@Lob
	@Column(length=2000) // allowing upto 2000 characters for description. [ with this you can add b/w 200-300 words]
	private String description;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDate joiningDate;
	
	@UpdateTimestamp
	private LocalDate lastUpdateDate;
	
	@Column(nullable = true)
	private boolean isGroup;
	
	
	/*
	 * NOTES: 
	 * 	This setup will create another table called 'USERS_SOCIAL_NETWORK', with columns [id, user_id, social_media_website, link]
	 *  where @userId is the foreign key to UserProfile entity.
	 *  @social_media_website is the map's key
	 *  @link is the map's value.
	*/
	@ElementCollection
	@MapKeyColumn(name = "SOCIAL_MEDIA_WEBSITE")
	@Column(nullable=true, name="LINK")
	@CollectionTable(name="USERS_SOCIAL_NETWORK", joinColumns=@JoinColumn(name="USER_ID"))
	private Map<String, String> socialMediaLinks;
	
	/*
	 * metadata to be added/removed to/from UserPostMetadata.class This field should
	 * never be updated from this class.
	 */
	@OneToMany(orphanRemoval=true, mappedBy="userProfile", fetch = FetchType.LAZY)
	private List<UserPostMetadata> userPostMetadata;
	
	@OneToMany(orphanRemoval=true, mappedBy="user", fetch = FetchType.LAZY)
	private Set<Follower> followers;
	
	@OneToMany(orphanRemoval= true, mappedBy = "user", fetch = FetchType.LAZY) 
	private Set<Following> following;
	
	@OneToMany(orphanRemoval= true, mappedBy = "user", fetch = FetchType.LAZY) 
	private Set<PrivateGroupMember> privateGroupMembers;
	
	@OneToMany(orphanRemoval=true, mappedBy="joiner", fetch = FetchType.LAZY)
	private List<PrivateGroupJoinRequest> privateGroupJoinRequests;
	
	
	/*
	 * data will be added from this class itself. Just remember to initialize the list before inserting for the firsttime/size == 0;
	*/
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name="users_followed_domains",
			joinColumns = {@JoinColumn(name = "user_id") },
			inverseJoinColumns= { @JoinColumn(name="domain_id")} 
			)
	private List<Domain> followedDomains;
	
/*
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name="user_group",
			joinColumns = {@JoinColumn(name = "user_id") },
			inverseJoinColumns= { @JoinColumn(name="group_id")} 
			)
	private List<PthoreGroup> followedGroups;
	
*/

	
	
	public Map<String, String> getSocialMediaLinks() {
		return socialMediaLinks;
	}

	public Long getLearningStreak() {
		return learningStreak;
	}

	public void setLearningStreak(Long learningStreak) {
		this.learningStreak = learningStreak;
	}

	public void setSocialMediaLinks(Map<String, String> socialMediaLinks) {
		this.socialMediaLinks = socialMediaLinks;
	}
	
	public List<PrivateGroupJoinRequest> getPrivateGroupJoinRequests() {
		return privateGroupJoinRequests;
	}

	public void setPrivateGroupJoinRequests(List<PrivateGroupJoinRequest> privateGroupJoinRequests) {
		this.privateGroupJoinRequests = privateGroupJoinRequests;
	}
	
	public String getProfilePicture() {
		return profilePicture;
	}
	
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public List<Domain> getFollowedDomains() {
		return followedDomains;
	}

	public void setFollowedDomains(List<Domain> followedDomains) {
		this.followedDomains = followedDomains;
	}
/*
	public List<PthoreGroup> getFollowedGroups() {
		return followedGroups;
	}

	public void setFollowedGroups(List<PthoreGroup> followedGroups) {
		this.followedGroups = followedGroups;
	}
*/
	
	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	
	public List<Domain> getDomains() {
		return followedDomains;
	}

	public void setDomains(List<Domain> domains) {
		this.followedDomains = domains;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public List<UserPostMetadata> getUserPostMetadata() {
		return userPostMetadata;
	}

	public void setUserPostMetadata(List<UserPostMetadata> userPostMetadata) {
		this.userPostMetadata = userPostMetadata;
	}

	public UserProfile() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public LocalDate getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDate lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Set<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<Follower> followers) {
		this.followers = followers;
	}

	public Set<Following> getFollowing() {
		return following;
	}

	public void setFollowing(Set<Following> following) {
		this.following = following;
	}

	public Set<PrivateGroupMember> getPrivateGroupMembers() {
		return privateGroupMembers;
	}

	public void setPrivateGroupMembers(Set<PrivateGroupMember> privateGroupMembers) {
		this.privateGroupMembers = privateGroupMembers;
	}



}
