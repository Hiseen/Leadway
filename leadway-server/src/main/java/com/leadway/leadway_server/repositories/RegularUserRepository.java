package com.leadway.leadway_server.repositories;
import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.RegularUser;

public interface RegularUserRepository extends CrudRepository<RegularUser, Long> {

}
