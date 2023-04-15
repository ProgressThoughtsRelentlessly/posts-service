package com.pthore.service.posts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.posts.entities.Domain;

public interface IDomainRepository extends JpaRepository<Domain, String> {
	public Domain findByDomainName(String domainName);
}
