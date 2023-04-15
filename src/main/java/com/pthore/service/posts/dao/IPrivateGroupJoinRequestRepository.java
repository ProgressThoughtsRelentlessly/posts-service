package com.pthore.service.posts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.posts.entities.PrivateGroupJoinRequest;

public interface IPrivateGroupJoinRequestRepository extends JpaRepository<PrivateGroupJoinRequest, Long>{

}
