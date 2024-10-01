package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Customer;
import com.maju_mundur.MajuMundur.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
    @Query("SELECT DISTINCT td.transaction.customer FROM TransactionDetail td WHERE td.product.merchant.id = :merchantId")
    List<Customer> findCustomersWhoBoughtFromMerchant(@Param("merchantId") String merchantId);

    @Query(value = "SELECT * FROM m_transaction_detail WHERE transaction_id = :id", nativeQuery = true)
    List<TransactionDetail> findByTransactionId(@Param("id")String id);
}
