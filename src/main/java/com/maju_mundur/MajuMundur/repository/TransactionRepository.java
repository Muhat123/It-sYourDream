package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}