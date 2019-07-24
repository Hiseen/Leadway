package com.leadway.leadway_server.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.EnterpriseUser;

public interface EnterpriseUserRepository extends CrudRepository<EnterpriseUser, Long> {

}
