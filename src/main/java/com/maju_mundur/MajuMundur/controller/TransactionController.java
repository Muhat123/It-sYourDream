package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.TransactionRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.TransactionResponse;
import com.maju_mundur.MajuMundur.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    private CommonResponse<TransactionResponse> generateTransactionResponse(Integer code, String message, Optional<TransactionResponse> transaction) {
        return CommonResponse.<TransactionResponse>builder()
                .statusCode(code)
                .message(message)
                .data(transaction)
                .build();
    }

    @PostMapping
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest);
        CommonResponse<TransactionResponse> commonResponse = generateTransactionResponse(HttpStatus.OK.value(), "Success add Transaction", Optional.of(transactionResponse));
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCustomerId() {
        List<TransactionResponse> transactionResponses = transactionService.getAllTransactionByCustomerId();
        return ResponseEntity.ok(transactionResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse transactionResponse = transactionService.getTransactionById(id);
        CommonResponse<TransactionResponse> commonResponse = generateTransactionResponse(HttpStatus.OK.value(), "Success get Transaction", Optional.of(transactionResponse));
        return ResponseEntity.ok(commonResponse);
    }

}
