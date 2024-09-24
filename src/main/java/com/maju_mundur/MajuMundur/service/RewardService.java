package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.request.RewardRequest;
import com.maju_mundur.MajuMundur.dto.response.RewardResponse;

import java.util.List;

public interface RewardService {
    RewardResponse create(RewardRequest customer);

    List<RewardResponse> getAll();

    RewardResponse getRewardById(String id);

    void deleteById(String id);

    void updateStatusById(String id);
}
