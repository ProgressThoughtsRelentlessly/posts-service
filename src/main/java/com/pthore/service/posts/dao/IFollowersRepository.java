package com.pthore.service.posts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.posts.entities.Follower;
import com.pthore.service.posts.entities.UserProfile;

public interface IFollowersRepository extends JpaRepository<Follower, Long>{

	void deleteByEmailAndUser(String email, UserProfile targetUser);

	void deleteByEmail(String email);

}
