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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${server.key.midtrans}")
    private String serverKey;

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
                .paymentUrl(transaction.getPaymentUrl())
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

        String paymentUrl = payTransaction(transaction.getId(), totalAmount, transactionDetails);
        transaction.setPaymentUrl(paymentUrl);
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

    public String payTransaction (String id, Double totalPayment, List<TransactionDetail> transactionDetails){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(user.getId());
        if (customer == null){
            throw new OurException("User not found");
        }

        String uniqueOrderId = transactionDetails.get(0).getTransaction().getId();
        String url = "https://app.sandbox.midtrans.com/snap/v1/transactions";
        String midtransServerKey = serverKey;
        Map<String, Object> params = new HashMap<>();

        Map<String, Object> transactionDetail = new HashMap<>();
        params.put("transaction_details", transactionDetail);
        transactionDetail.put("order_id", uniqueOrderId);
        transactionDetail.put("gross_amount", totalPayment);

        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", customer.getName());
        customerDetails.put("email", customer.getEmail());
        params.put("customer_details", customerDetails);

        List<Map<String, Object>> itemDetails = new ArrayList<>();
        for (TransactionDetail detail : transactionDetails){
            Map<String, Object> item = new HashMap<>();
            item.put("id", detail.getId());
            item.put("price", detail.getProduct().getPrice());
            item.put("quantity", detail.getQuantity());
            item.put("name", detail.getProduct().getName());
            itemDetails.add(item);
        };
        params.put("item_details", itemDetails);
        Map<String, Object> creditCard = new HashMap<>();
        params.put("credit_card", creditCard);
        creditCard.put("secure", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((midtransServerKey + ":").getBytes()));

        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(params, headers), Map.class
        );
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("redirect_url")) {
            return (String) responseBody.get("redirect_url");
        } else {
            String errorMessage = responseBody != null ? responseBody.toString() : "No response body";
            throw new RuntimeException("Failed to create transaction: " + errorMessage);
        }
    }



}
