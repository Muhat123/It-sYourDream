package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.RewardRequest;
import com.maju_mundur.MajuMundur.dto.Response.RedeemResponse;
import com.maju_mundur.MajuMundur.dto.Response.RewardResponse;
import org.springframework.stereotype.Repository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Repository
public interface RewardService {
    RewardResponse createReward(RewardRequest rewardRequest) throws AccessDeniedException;

    List<RewardResponse> getAllReward();

    RewardResponse getRewardById(String id);

    RewardResponse updateRewardById(RewardRequest rewardRequest) throws AccessDeniedException;

    String deleteRewardById(String rewardId) throws AccessDeniedException;
}
