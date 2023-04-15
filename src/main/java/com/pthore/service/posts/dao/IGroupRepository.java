package com.pthore.service.posts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.posts.entities.PthoreGroup;

public interface IGroupRepository extends JpaRepository<PthoreGroup, Long> {

}
