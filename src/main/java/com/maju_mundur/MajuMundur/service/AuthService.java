package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Request.LoginRequest;
import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Request.RegisterRequest;
import com.maju_mundur.MajuMundur.dto.Response.LoginResponse;
import com.maju_mundur.MajuMundur.dto.Response.RegisterResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthService {


    RegisterResponse registerCustomer(RegisterRequest<CustomerRequest> registerRequest);

    RegisterResponse registerMerchant(RegisterRequest<MerchantRequest> registerRequest);

    LoginResponse login(LoginRequest request);
}
