package com.fqrmix.authcenterback.repositories;

import com.fqrmix.authcenterback.models.EService;
import com.fqrmix.authcenterback.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(EService name);
}
