package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Request.LoginRequest;
import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Request.RegisterRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.LoginResponse;
import com.maju_mundur.MajuMundur.dto.Response.RegisterResponse;
import com.maju_mundur.MajuMundur.service.AuthService;
import com.maju_mundur.MajuMundur.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register/customer")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCustomer(@RequestBody RegisterRequest<CustomerRequest> registerRequest) {
        RegisterResponse registered = authService.registerCustomer(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(HttpStatus.CREATED.value(), Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/merchant")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerMerchant(@RequestBody RegisterRequest<MerchantRequest> registerRequest) {
        RegisterResponse registered = authService.registerMerchant(registerRequest);
        CommonResponse<RegisterResponse> response = generateRegisterResponse(HttpStatus.CREATED.value(), Optional.of(registered));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse login = authService.login(loginRequest);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Success")
                .data(Optional.of(login))
                .build();

        return ResponseEntity.ok(response);

    }

    private CommonResponse<RegisterResponse> generateRegisterResponse(Integer code, Optional<RegisterResponse> registerResponse) {
        return CommonResponse.<RegisterResponse>builder()
                .statusCode(code)
                .message("Account Successfully Registered!")
                .data(registerResponse)
                .build();
    }
}
