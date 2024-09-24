package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Response.MerchantResponse;
import com.maju_mundur.MajuMundur.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantService {
    MerchantResponse create(MerchantRequest merchant, User user);

    List<MerchantResponse> getAll();

    MerchantResponse getMerchantById(String id);

    void deleteById(String id);

    void updateStatusById(String id);
}
