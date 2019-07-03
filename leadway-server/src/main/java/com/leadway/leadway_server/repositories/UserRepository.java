package com.leadway.leadway_server.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.LeadwayUser;

public interface UserRepository extends CrudRepository<LeadwayUser, Long> {
	
	List<LeadwayUser> findByEmail(String email);
	
	List<LeadwayUser> findByEmailAndPassword(String email, String password);

	List<LeadwayUser> findById(long id);
}
