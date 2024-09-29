package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Response.MerchantResponse;
import com.maju_mundur.MajuMundur.entity.Merchant;
import com.maju_mundur.MajuMundur.entity.User;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.MerchantRepository;
import com.maju_mundur.MajuMundur.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;

    @Override
    public MerchantResponse create(MerchantRequest merchantRequest, User user) {
        Merchant newMerchant = Merchant.builder()
                .name(merchantRequest.getName())
                .email(merchantRequest.getEmail())
                .phone(merchantRequest.getPhone())
                .user(user)
                .build();

        Merchant savedMerchant = merchantRepository.saveAndFlush(newMerchant);

        return mapToResponse(savedMerchant);
    }

    @Override
    public List<MerchantResponse> getAll() {
        List<Merchant> merchants = merchantRepository.findAll();
        return merchants.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MerchantResponse getMerchantById(String id) {
        Optional<Merchant> merchantOpt = merchantRepository.findById(id);

        return merchantOpt.map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
    }

    @Override
    public void deleteById(String id) {
        Optional<Merchant> merchantOpt = merchantRepository.findById(id);
        merchantOpt.ifPresent(merchantRepository::delete);
    }

    @Override
    public void updateStatusById(String id) {
        Optional<Merchant> merchantOpt = merchantRepository.findById(id);
        if (merchantOpt.isPresent()) {
            Merchant merchant = merchantOpt.get();
            merchantRepository.save(merchant);
        } else {
            throw new RuntimeException("Merchant not found");
        }
    }

    @Override
    public MerchantResponse updateMerchantById(MerchantRequest merchantRequest){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantRepository.findByUserId(loggedInUser.getId());
        if (merchant == null) {
            throw new OurException("Merchant not found");
        }
        if (!merchant.getId().equals(merchantRequest.getId())) {
            throw new OurException("Unauthorized access: You can only update your own merchant data");
        }
        if (!merchantRequest.getName().isBlank()) {
            merchant.setName(merchantRequest.getName());
        }
        if (!merchantRequest.getEmail().isBlank()) {
            merchant.setEmail(merchantRequest.getEmail());
        }
        if (!merchantRequest.getPhone().isBlank()) {
            merchant.setPhone(merchantRequest.getPhone());
        }
        Merchant savedMerchant = merchantRepository.save(merchant);

        return mapToResponse(savedMerchant);
    }

    // Helper method to map Merchant entity to MerchantResponse DTO
    private MerchantResponse mapToResponse(Merchant merchant) {
        return MerchantResponse.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .email(merchant.getEmail())
                .phone(merchant.getPhone())
                .build();
    }
}
