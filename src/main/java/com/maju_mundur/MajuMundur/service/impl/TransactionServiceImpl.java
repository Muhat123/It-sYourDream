package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.TransactionDetailRequest;
import com.maju_mundur.MajuMundur.dto.Request.TransactionRequest;
import com.maju_mundur.MajuMundur.dto.Response.TransactionDetailResponse;
import com.maju_mundur.MajuMundur.dto.Response.TransactionResponse;
import com.maju_mundur.MajuMundur.entity.*;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.CustomerRepository;
import com.maju_mundur.MajuMundur.repository.ProductRepository;
import com.maju_mundur.MajuMundur.repository.TransactionDetailRepository;
import com.maju_mundur.MajuMundur.repository.TransactionRepository;
import com.maju_mundur.MajuMundur.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final TransactionDetailRepository transactionDetailRepository;

    private TransactionResponse generateTransactionResponse(Transaction transaction) {
        List<TransactionDetailResponse> transactionDetailResponseList = new ArrayList<>();
        List<TransactionDetail> transactionDetailList = transaction.getTransactionDetails();
        for (int i = 0; i < transactionDetailList.size(); i++) {
            TransactionDetailResponse transactionDetailResponse = TransactionDetailResponse.builder()
                    .id(transactionDetailList.get(i).getId())
                    .quantity(transactionDetailList.get(i).getQuantity())
                    .price(transactionDetailList.get(i).getProduct().getPrice())
                    .productId(transactionDetailList.get(i).getProduct().getId())
                    .build();
            transactionDetailResponseList.add(transactionDetailResponse);
        }
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .customerId(transaction.getCustomer().getId())
                .transactionDetails(transactionDetailResponseList)
                .build();
    }


    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .build();
        transactionRepository.save(transaction);

        List<TransactionDetailRequest> transactionDetailRequestList = transactionRequest.getTransactionDetailRequestList();
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        double totalAmount = 0;
        for (TransactionDetailRequest transactionDetailRequest : transactionDetailRequestList) {
            Product product = productRepository.findById(transactionDetailRequest.getProductId()).orElseThrow(() -> new OurException("Product tidak ada"));

            if (product.getQuantity() < transactionDetailRequest.getQuantity()){
                throw new OurException("Stok barang tidak mencukup untuk transaksi, sisa: " + product.getQuantity());
            }
            TransactionDetail transactionDetail = TransactionDetail.builder()
                    .transaction(transaction)
                    .product(product)
                    .quantity(transactionDetailRequest.getQuantity())
                    .build();

            transactionDetails.add(transactionDetail);
            transactionDetailRepository.save(transactionDetail);
            product.setQuantity(product.getQuantity() - transactionDetailRequest.getQuantity());
            productRepository.save(product);

            totalAmount += transactionDetailRequest.getQuantity() * product.getPrice();

        }

        transaction.setTransactionDetails(transactionDetails);
        transactionRepository.save(transaction);
        return generateTransactionResponse(transaction);

    }

    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new OurException("Transaksi tidak ditemukan"));
        return generateTransactionResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getAllTransactionByCustomerId() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        List<Transaction> allTransactionById =  transactionRepository.findTransactionByCustomerId(customer.getId());
        List<TransactionResponse> transactionResponseList = new ArrayList<>();
        for (Transaction transaction : allTransactionById) {
            transactionResponseList.add(generateTransactionResponse(transaction));
        }
        return transactionResponseList;
    }
}
