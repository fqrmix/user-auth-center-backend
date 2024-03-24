package com.fqrmix.authcenterback.repositories;

import com.fqrmix.authcenterback.models.enums.ERole;
import com.fqrmix.authcenterback.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
