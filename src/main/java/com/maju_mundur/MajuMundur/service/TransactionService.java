package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.TransactionRequest;
import com.maju_mundur.MajuMundur.dto.Response.TransactionResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
    TransactionResponse getTransactionById(String transactionId);
    List<TransactionResponse> getAllTransactionByCustomerId();
    void setTransactionStatus(Map<String, Object> notification);
}
