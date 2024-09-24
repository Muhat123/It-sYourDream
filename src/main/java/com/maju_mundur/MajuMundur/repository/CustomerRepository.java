package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.dto.response.CustomerResponse;
import com.maju_mundur.MajuMundur.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}