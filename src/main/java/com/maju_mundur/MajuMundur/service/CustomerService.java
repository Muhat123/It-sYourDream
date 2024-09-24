package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Response.CustomerResponse;
import com.maju_mundur.MajuMundur.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerService {

    CustomerResponse create(CustomerRequest customer, User user);

    List<CustomerResponse> getAll();

    CustomerResponse getCustomerById(String id);

    void deleteById(String id);

    void updateStatusById(String id);
}
