package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest customer);

    List<CustomerResponse> getAll();

    CustomerResponse getCustomerById(String id);

    void deleteById(String id);

    void updateStatusById(String id);
}
