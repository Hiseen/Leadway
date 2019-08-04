package com.leadway.leadway_server.repositories;

import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.LeadwayTask;

public interface TaskRepository extends CrudRepository<LeadwayTask, Long>{

}
