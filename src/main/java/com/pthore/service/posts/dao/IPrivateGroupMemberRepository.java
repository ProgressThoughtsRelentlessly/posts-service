package com.pthore.service.posts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.posts.entities.PrivateGroupMember;

public interface IPrivateGroupMemberRepository extends JpaRepository<PrivateGroupMember, Long> {

}
