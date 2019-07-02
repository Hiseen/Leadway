package com.leadway.leadway_server.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.LeadwayUser;

public interface UserRepository extends CrudRepository<LeadwayUser, Long> {

}
