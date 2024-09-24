package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.constant.ApiUrl;
import com.maju_mundur.MajuMundur.dto.response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.response.CustomerResponse;
import com.maju_mundur.MajuMundur.dto.response.RewardResponse;
import com.maju_mundur.MajuMundur.entity.Reward;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.REWARD)
public class RewardController {
    private final RewardController rewardController;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<RewardResponse>> getAllReward(){
        return null;
    }
}
