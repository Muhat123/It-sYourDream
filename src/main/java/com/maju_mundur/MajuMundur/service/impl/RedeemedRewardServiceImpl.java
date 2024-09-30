package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.RedeemRequest;
import com.maju_mundur.MajuMundur.dto.Response.RedeemResponse;
import com.maju_mundur.MajuMundur.entity.Customer;
import com.maju_mundur.MajuMundur.entity.RedeemedReward;
import com.maju_mundur.MajuMundur.entity.Reward;
import com.maju_mundur.MajuMundur.entity.User;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.CustomerRepository;
import com.maju_mundur.MajuMundur.repository.RedeemRepository;
import com.maju_mundur.MajuMundur.repository.RewardRepository;
import com.maju_mundur.MajuMundur.service.RedeemedRewardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedeemedRewardServiceImpl implements RedeemedRewardService {
    private final RedeemRepository redeemRepository;
    private final CustomerRepository customerRepository;
    private final RewardRepository rewardRepository;

    private RedeemResponse generateToRedeemResponse(RedeemedReward redeemedReward, String message) {
        return RedeemResponse.builder()
                .id(redeemedReward.getId())
                .rewardId(redeemedReward.getReward().getId())
                .rewardName(redeemedReward.getReward().getName())
                .pointRedeemed(redeemedReward.getReward().getPointValue())
                .redeemedAt(redeemedReward.getRedeemAt())
                .message(message)
                .build();
    }


    @Override
    @Transactional
    public RedeemResponse redeemPoint(RedeemRequest redeemRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        if (customer == null) {
            throw new RuntimeException("Customer not found for user id: " + loggedInUser.getId());
        }
        Reward reward = rewardRepository.findById(redeemRequest.getRewardId()).orElseThrow(() -> new OurException("Reward not found"));
        if (customer.getPoints() < reward.getPointValue()) {
            throw new OurException("Insufficient point");
        }
        customer.setPoints(customer.getPoints() - reward.getPointValue());
        customerRepository.save(customer);
        RedeemedReward redeemedReward = RedeemedReward.builder()
                .customer(customer)
                .reward(reward)
                .redeemAt(LocalDateTime.now())
                .build();
        redeemRepository.save(redeemedReward);
        return generateToRedeemResponse(redeemedReward, "Success redeem reward with point " + reward.getPointValue() + " with the reward " + reward.getName() + " your point now is " + customer.getPoints());
    }
}
