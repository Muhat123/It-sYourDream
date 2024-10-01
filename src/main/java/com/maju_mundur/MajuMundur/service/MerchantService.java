package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Response.CustomerResponse;
import com.maju_mundur.MajuMundur.dto.Response.MerchantResponse;
import com.maju_mundur.MajuMundur.entity.Customer;
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

    MerchantResponse updateMerchantById(MerchantRequest merchantRequest);

    List<CustomerResponse> getCustomersWhoBoughtFromMerchant(String merchantId);
}
