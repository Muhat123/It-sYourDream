package com.maju_mundur.MajuMundur.dto.Response;

import com.maju_mundur.MajuMundur.entity.Product;
import com.maju_mundur.MajuMundur.entity.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailResponse {

    private String id;
    private String productId;
    private Double price;
    private Integer quantity;

}
