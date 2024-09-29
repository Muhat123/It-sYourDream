package com.maju_mundur.MajuMundur.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardResponse {
    private String id;
    private String name;
    private Double pointValue;
}