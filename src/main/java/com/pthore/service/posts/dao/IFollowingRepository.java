package com.pthore.service.posts.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pthore.service.posts.entities.Following;

@Repository
public interface IFollowingRepository extends  JpaRepository<Following, Long> {

}
