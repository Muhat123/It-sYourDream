package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.RedeemedReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeemRepository extends JpaRepository<RedeemedReward, String> {
}
