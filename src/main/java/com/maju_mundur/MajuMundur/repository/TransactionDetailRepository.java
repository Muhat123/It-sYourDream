package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
}
