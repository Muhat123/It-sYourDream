package com.maju_mundur.MajuMundur.dto.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private LocalDateTime transactionDate;
    private String customerId;
    private List<TransactionDetailResponse> transactionDetails;
    private String paymentUrl;
    private Double pointPerTransaction;
    private Double totalAmount;
}
