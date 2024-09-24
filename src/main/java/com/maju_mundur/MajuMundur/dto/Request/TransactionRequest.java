package com.maju_mundur.MajuMundur.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private String id;
    private String customerId;
    private List<TransactionDetailRequest> transactionDetailRequestList;
}
