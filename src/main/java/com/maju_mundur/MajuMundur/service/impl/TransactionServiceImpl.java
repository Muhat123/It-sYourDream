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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final TransactionDetailRepository transactionDetailRepository;

    private TransactionResponse generateTransactionResponse(Transaction transaction, Customer customer) {
        List<TransactionDetailResponse> transactionDetailResponseList = new ArrayList<>();
        List<TransactionDetail> transactionDetailList = transaction.getTransactionDetails();
        int i;
        double totalAmount = 0;
        for (TransactionDetail transactionDetail : transactionDetailList) {
            TransactionDetailResponse transactionDetailResponse = TransactionDetailResponse.builder()
                    .id(transactionDetail.getId())
                    .name(transactionDetail.getProduct().getName())
                    .quantity(transactionDetail.getQuantity())
                    .price(transactionDetail.getProduct().getPrice())
                    .productId(transactionDetail.getProduct().getId())
                    .build();
            transactionDetailResponseList.add(transactionDetailResponse);
            totalAmount += transactionDetail.getQuantity() * transactionDetail.getProduct().getPrice();
        }
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .customerId(customer.getId())
                .transactionDetails(transactionDetailResponseList)
                .transactionDate(LocalDateTime.now())
                .pointPerTransaction(transaction.getPointPerTransaction())
                .totalAmount(totalAmount)
                .build();
    }


    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());

        // Initialize Transaction object but don't save it yet.
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .build();

        List<TransactionDetailRequest> transactionDetailRequestList = transactionRequest.getTransactionDetailRequestList();
        List<TransactionDetail> transactionDetails = new ArrayList<>();

        double totalAmount = 0;

        // Process all TransactionDetails and accumulate the total amount
        for (TransactionDetailRequest transactionDetailRequest : transactionDetailRequestList) {
            Product product = productRepository.findById(transactionDetailRequest.getProductId())
                    .orElseThrow(() -> new OurException("Product not found"));

            if (product.getQuantity() < transactionDetailRequest.getQuantity()) {
                throw new OurException("Insufficient stock, remaining: " + product.getQuantity());
            }

            // Create TransactionDetail but do NOT save it yet
            TransactionDetail transactionDetail = TransactionDetail.builder()
                    .transaction(transaction)  // Link it to the parent Transaction
                    .product(product)
                    .quantity(transactionDetailRequest.getQuantity())
                    .build();

            transactionDetails.add(transactionDetail);
            product.setQuantity(product.getQuantity() - transactionDetailRequest.getQuantity());
            productRepository.save(product);

            // Accumulate total amount
            totalAmount += transactionDetailRequest.getQuantity() * product.getPrice();
        }

        // Set details and total amount for the Transaction before saving
        transaction.setTransactionDetails(transactionDetails);
        transaction.setCustomer(customer);
        transaction.setPointPerTransaction(0.1); // Example point logic
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTotalAmount(totalAmount);

        // Save the Transaction (this will cascade save the TransactionDetails as well)
        transactionRepository.save(transaction);

        // Increment customer points
        customer.setPoints(customer.getPoints() + 0.1);
        customerRepository.save(customer);

        // Generate response
        return generateTransactionResponse(transaction, customer);
    }


    @Override
    public TransactionResponse getTransactionById(String transactionId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new OurException("Transaksi tidak ditemukan"));
        return generateTransactionResponse(transaction, customer);
    }

    @Override
    public List<TransactionResponse> getAllTransactionByCustomerId() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        List<Transaction> allTransactionById =  transactionRepository.findTransactionByCustomerId(customer.getId());
        if (allTransactionById.isEmpty()) {
            throw new OurException("No transaction with this customer id " + customer.getId());
        }
        List<TransactionResponse> transactionResponseList = new ArrayList<>();
        for (Transaction transaction : allTransactionById) {
            transactionResponseList.add(generateTransactionResponse(transaction, customer));
        }
        return transactionResponseList;
    }
}
