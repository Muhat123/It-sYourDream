package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.request.RewardRequest;
import com.maju_mundur.MajuMundur.dto.response.RewardResponse;
import com.maju_mundur.MajuMundur.entity.Reward;
import com.maju_mundur.MajuMundur.repository.RewardRepository; // Pastikan kamu memiliki repository ini
import com.maju_mundur.MajuMundur.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository; // Ganti dengan repository yang sesuai

    @Override
    public RewardResponse create(RewardRequest rewardRequest) {
        // Membuat entitas Reward dari request
        Reward newReward = Reward.builder()
                .name(rewardRequest.getName())
                .pointValue(rewardRequest.getPointValue())
                .build();

        // Menyimpan reward ke dalam database
        Reward savedReward = rewardRepository.saveAndFlush(newReward);

        // Mengembalikan response
        return mapToResponse(savedReward);
    }

    @Override
    public List<RewardResponse> getAll() {
        // Mengambil semua reward dari database
        List<Reward> rewards = rewardRepository.findAll();

        // Mengubah ke List<RewardResponse>
        return rewards.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RewardResponse getRewardById(String id) {
        // Mencari reward berdasarkan ID
        Optional<Reward> rewardOpt = rewardRepository.findById(id);

        // Jika ditemukan, kembalikan sebagai response
        return rewardOpt.map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
    }

    @Override
    public void deleteById(String id) {
        // Mencari dan menghapus reward berdasarkan ID
        Optional<Reward> rewardOpt = rewardRepository.findById(id);
        rewardOpt.ifPresent(rewardRepository::delete);
    }

    @Override
    public void updateById(String id, RewardRequest rewardRequest) {
        // Mencari reward berdasarkan ID
        Optional<Reward> rewardOpt = rewardRepository.findById(id);

        if (rewardOpt.isPresent()) {
            Reward existingReward = rewardOpt.get();

            // Update fields dari RewardRequest jika ada
            existingReward.setName(rewardRequest.getName());
            existingReward.setPointValue(rewardRequest.getPointValue());

            // Simpan perubahan ke repository
            rewardRepository.save(existingReward);
        } else {
            throw new RuntimeException("Reward not found");
        }
    }

    // Helper method untuk mengubah Reward entity menjadi RewardResponse DTO
    private RewardResponse mapToResponse(Reward reward) {
        return RewardResponse.builder()
                .name(reward.getName())
                .pointValue(reward.getPointValue())
                .build();
    }
}
