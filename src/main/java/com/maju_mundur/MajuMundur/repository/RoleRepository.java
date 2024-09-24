package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(Role.UserRole name);
}
