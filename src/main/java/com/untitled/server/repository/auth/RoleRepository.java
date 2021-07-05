package com.untitled.server.repository.auth;

import java.util.Optional;

import com.untitled.server.domain.auth.ERole;
import com.untitled.server.domain.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}