package com.maju_mundur.MajuMundur.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardRequest {
    private String id;
    private String name;
    private Double pointValue;
}
