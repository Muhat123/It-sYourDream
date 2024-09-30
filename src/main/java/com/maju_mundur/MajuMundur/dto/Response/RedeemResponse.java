package com.maju_mundur.MajuMundur.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedeemResponse {
    private String id;
    private String rewardId;
    private String rewardName;
    private Double pointRedeemed;
    private LocalDateTime redeemedAt;
    private String message;
}
