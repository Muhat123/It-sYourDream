package com.maju_mundur.MajuMundur.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String id;
    private String name;
    private Double price;
    private Integer quantity;
    private String description;
}
