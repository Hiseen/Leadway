package com.leadway.leadway_server.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.leadway.leadway_server.entities.AutoLoginData;

public interface AutoLoginDataRepository extends CrudRepository<AutoLoginData, Long> {
    Optional<AutoLoginData> findById(Long id);
    
    List<AutoLoginData> findByExpirationTimeLessThan(long expirationTime);
}

