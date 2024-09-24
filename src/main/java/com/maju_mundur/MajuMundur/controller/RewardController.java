package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.constant.ApiUrl;
import com.maju_mundur.MajuMundur.dto.request.RewardRequest;
import com.maju_mundur.MajuMundur.dto.response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.response.RewardResponse;
import com.maju_mundur.MajuMundur.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.REWARD)
public class RewardController {
    private final RewardService rewardService;

    // Create a new Reward
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<RewardResponse>> createReward(@RequestBody RewardRequest rewardRequest) {
        RewardResponse rewardResponse = rewardService.create(rewardRequest);
        CommonResponse<RewardResponse> commonResponse = CommonResponse.<RewardResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Reward created successfully!")
                .data(rewardResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    // Get all Rewards
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<RewardResponse>>> getAllRewards() {
        List<RewardResponse> rewards = rewardService.getAll();
        CommonResponse<List<RewardResponse>> commonResponse = CommonResponse.<List<RewardResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("List of rewards")
                .data(rewards)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Get Reward by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<RewardResponse>> getRewardById(@PathVariable String id) {
        RewardResponse rewardResponse = rewardService.getRewardById(id);
        CommonResponse<RewardResponse> commonResponse = CommonResponse.<RewardResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reward found")
                .data(rewardResponse)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Update Reward by ID
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> updateRewardById(@PathVariable String id, @RequestBody RewardRequest rewardRequest) {
        rewardService.updateById(id, rewardRequest);
        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reward updated successfully")
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Delete Reward by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteRewardById(@PathVariable String id) {
        rewardService.deleteById(id);
        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reward deleted successfully")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
}
