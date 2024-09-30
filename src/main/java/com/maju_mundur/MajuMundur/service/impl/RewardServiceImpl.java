package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.RewardRequest;
import com.maju_mundur.MajuMundur.dto.Response.RedeemResponse;
import com.maju_mundur.MajuMundur.dto.Response.RewardResponse;
import com.maju_mundur.MajuMundur.entity.Customer;
import com.maju_mundur.MajuMundur.entity.Reward;
import com.maju_mundur.MajuMundur.entity.User;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.CustomerRepository;
import com.maju_mundur.MajuMundur.repository.RewardRepository;
import com.maju_mundur.MajuMundur.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository rewardRepository;
    private final CustomerRepository customerRepository;

    private RewardResponse generateToResponse(Reward reward) {
        return RewardResponse.builder()
                .id(reward.getId())
                .name(reward.getName())
                .pointValue(reward.getPointValue())
                .build();
    }

    @Override
    public RewardResponse createReward(RewardRequest rewardRequest) throws AccessDeniedException {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //check whether the user is admin
        boolean isAdmin = loggedInUser.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Unauthorized access");
        }

        Reward reward = Reward.builder()
                .name(rewardRequest.getName())
                .pointValue(rewardRequest.getPointValue())
                .build();
        rewardRepository.save(reward);
        return generateToResponse(reward);
    }

    @Override
    public List<RewardResponse> getAllReward() {
        List<Reward> rewardList = rewardRepository.findAll();
        return rewardList.stream().map(this::generateToResponse).toList();
    }

    @Override
    public RewardResponse getRewardById(String id) {
        return rewardRepository.findById(id).map(this::generateToResponse).orElseThrow(() -> new RuntimeException("Reward not found"));
    }

    @Override
    public RewardResponse updateRewardById(RewardRequest rewardRequest) throws AccessDeniedException {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = loggedInUser.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Unauthorized access");
        }
        Reward reward = rewardRepository.findById(rewardRequest.getId()).orElseThrow(() -> new OurException("Reward not found wit id " + rewardRequest.getId()));

        //change reward name
        if (rewardRequest.getName().isBlank()) {
            reward.setName(reward.getName());
        } else {
            reward.setName(rewardRequest.getName());
        }

        //change point value
        if (rewardRequest.getPointValue() == null) {
            reward.setPointValue(reward.getPointValue());
        } else {
            reward.setPointValue(rewardRequest.getPointValue());
        }

        return generateToResponse(rewardRepository.save(reward));
    }

    @Override
    public String deleteRewardById(String rewardId) throws AccessDeniedException {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = loggedInUser.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Unauthorized access");
        }
        rewardRepository.deleteById(rewardId);
        return "Reward with id " + rewardId + " has been deleted";
    }

}
