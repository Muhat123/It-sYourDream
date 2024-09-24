package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
