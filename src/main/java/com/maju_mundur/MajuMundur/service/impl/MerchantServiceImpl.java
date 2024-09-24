package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.response.MerchantResponse;
import com.maju_mundur.MajuMundur.entity.Merchant;
import com.maju_mundur.MajuMundur.repository.MerchantRepository;
import com.maju_mundur.MajuMundur.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;

    @Override
    public MerchantResponse create(MerchantRequest merchantRequest) {
        // Membuat entitas Merchant berdasarkan request
        Merchant newMerchant = Merchant.builder()
                .name(merchantRequest.getName())
                .email(merchantRequest.getEmail())
                .phone(merchantRequest.getPhone())
                .build();

        // Menyimpan entitas Merchant ke database
        Merchant savedMerchant = merchantRepository.saveAndFlush(newMerchant);

        // Mengembalikan response
        return mapToResponse(savedMerchant);
    }

    @Override
    public List<MerchantResponse> getAll() {
        // Mengambil semua Merchant dari database
        List<Merchant> merchants = merchantRepository.findAll();

        // Mengubah List<Merchant> menjadi List<MerchantResponse>
        return merchants.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MerchantResponse getMerchantById(String id) {
        // Mencari Merchant berdasarkan ID
        Optional<Merchant> merchantOpt = merchantRepository.findById(id);

        // Jika Merchant ditemukan, ubah menjadi response
        return merchantOpt.map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
    }

    @Override
    public void deleteById(String id) {
        // Mencari Merchant berdasarkan ID dan menghapusnya
        Optional<Merchant> merchantOpt = merchantRepository.findById(id);
        merchantOpt.ifPresent(merchantRepository::delete);
    }

    @Override
    public void updateById(String id, MerchantRequest merchantRequest) {
        // Mencari Merchant berdasarkan ID
        Optional<Merchant> merchantOpt = merchantRepository.findById(id);

        if (merchantOpt.isPresent()) {
            Merchant existingMerchant = merchantOpt.get();

            // Update fields berdasarkan data dari MerchantRequest
            existingMerchant.setName(merchantRequest.getName());
            existingMerchant.setEmail(merchantRequest.getEmail());
            existingMerchant.setPhone(merchantRequest.getPhone());

            // Simpan perubahan ke repository
            merchantRepository.save(existingMerchant);
        } else {
            throw new RuntimeException("Merchant not found");
        }
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
