package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.RedeemRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.RedeemResponse;
import com.maju_mundur.MajuMundur.service.RedeemedRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/redeem")
@RequiredArgsConstructor
public class RedeemRewardController {
    private final RedeemedRewardService rewardService;

    private <T> CommonResponse<T> generateToRedeemRewardResponse(Integer code, String message, Optional<T> data) {
        return CommonResponse.<T>builder()
                .statusCode(code)
                .message(message)
                .data(data)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<RedeemResponse>> redeemReward(@RequestBody RedeemRequest redeemRequest){
        RedeemResponse response = rewardService.redeemPoint(redeemRequest);
        CommonResponse<RedeemResponse> commonResponse = generateToRedeemRewardResponse(HttpStatus.CREATED.value(), "Success Redeem Reward", Optional.of(response));
        return ResponseEntity.ok(commonResponse);
    }
}
