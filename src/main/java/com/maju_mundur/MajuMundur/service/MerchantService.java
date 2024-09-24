package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.response.MerchantResponse;

import java.util.List;

public interface MerchantService {
    MerchantResponse create(MerchantRequest merchant);

    List<MerchantResponse> getAll();

    MerchantResponse getMerchantById(String id);

    void deleteById(String id);

    void updateStatusById(String id);
}
