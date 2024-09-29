package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.RewardRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.RewardResponse;
import com.maju_mundur.MajuMundur.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {
    private <T> CommonResponse<T> generateToRewardResponse(Integer code, String message, Optional<T> data) {
        return CommonResponse.<T>builder()
                .statusCode(code)
                .message(message)
                .data(data)
                .build();
    }
    private <T> CommonResponse<T> generateStringResponse(Integer code, String message) {
        return CommonResponse.<T>builder()
                .statusCode(code)
                .message(message)
                .build();
    }

    private final RewardService rewardService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<RewardResponse>> createReward(@RequestBody RewardRequest rewardRequest) throws AccessDeniedException {
        RewardResponse rewardResponse = rewardService.createReward(rewardRequest);
        CommonResponse<RewardResponse> response = generateToRewardResponse(HttpStatus.CREATED.value(), "Success created reward", Optional.of(rewardResponse));
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<RewardResponse>> updateRewardById(@RequestBody RewardRequest rewardRequest) throws AccessDeniedException {
        RewardResponse rewardResponse = rewardService.updateRewardById(rewardRequest);
        CommonResponse<RewardResponse> response = generateToRewardResponse(HttpStatus.OK.value(), "Success updated reward", Optional.of(rewardResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<CommonResponse<List<RewardResponse>>> getAllReward(){
        List<RewardResponse> rewardResponseList = rewardService.getAllReward();
        CommonResponse<List<RewardResponse>> response = generateToRewardResponse(HttpStatus.OK.value(), "Success get all rewards", Optional.of(rewardResponseList));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{rewardId}")
    public ResponseEntity<CommonResponse<RewardResponse>> getRewardById(@PathVariable String rewardId){
        RewardResponse response = rewardService.getRewardById(rewardId);
        CommonResponse<RewardResponse> commonResponse = generateToRewardResponse(HttpStatus.OK.value(), "Success get reward with id " + rewardId, Optional.of(response));
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/{rewardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<String>> deleteRewardById(@PathVariable String rewardId) throws AccessDeniedException {
        String deletedReward = rewardService.deleteRewardById(rewardId);
        CommonResponse<String> commonResponse = generateStringResponse(HttpStatus.NO_CONTENT.value(), "Success delete reward with id " + rewardId);
        return ResponseEntity.ok(commonResponse);
    }
}
