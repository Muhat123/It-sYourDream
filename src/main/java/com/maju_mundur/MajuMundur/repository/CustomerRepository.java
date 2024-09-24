package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByUserId(String userId);
}
