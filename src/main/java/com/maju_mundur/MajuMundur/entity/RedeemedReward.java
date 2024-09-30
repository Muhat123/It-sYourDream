package com.maju_mundur.MajuMundur.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_redeemed_reward")
public class RedeemedReward {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name="reward_id")
    private Reward reward;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Jakarta")
    private LocalDateTime redeemAt;
}
