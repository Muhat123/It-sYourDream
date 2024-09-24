package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}