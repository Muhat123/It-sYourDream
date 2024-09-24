package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.dto.Response.TransactionResponse;
import com.maju_mundur.MajuMundur.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findTransactionByCustomerId(String customerId);
}
