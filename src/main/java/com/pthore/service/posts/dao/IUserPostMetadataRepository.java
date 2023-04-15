package com.pthore.service.posts.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.posts.entities.UserPostMetadata;
import com.pthore.service.posts.entities.UserProfile;

public interface IUserPostMetadataRepository extends JpaRepository<UserPostMetadata, Long> {

	public List<UserPostMetadata> findByPostIdIn(List<String> postIds);

	public List<UserPostMetadata> findByUserProfile(UserProfile userProfile);

}
