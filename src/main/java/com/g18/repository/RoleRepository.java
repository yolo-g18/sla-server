package com.g18.repository;

import com.g18.entity.Role;
import com.g18.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
