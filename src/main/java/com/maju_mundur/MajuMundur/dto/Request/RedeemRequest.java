package com.maju_mundur.MajuMundur.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedeemRequest {
    private String customerId;
    private String rewardId;
}
