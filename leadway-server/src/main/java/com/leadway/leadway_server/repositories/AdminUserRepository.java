package com.leadway.leadway_server.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.AdminUser;

public interface AdminUserRepository extends CrudRepository<AdminUser, Long> {

}
