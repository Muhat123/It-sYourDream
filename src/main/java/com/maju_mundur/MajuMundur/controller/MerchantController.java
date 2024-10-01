package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.CustomerResponse;
import com.maju_mundur.MajuMundur.dto.Response.MerchantResponse;
import com.maju_mundur.MajuMundur.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merchant")
public class MerchantController {
    private final MerchantService merchantService;

    private <T> CommonResponse<T> generateToCustomerResponse(Integer code, String message, Optional<T> data) {
        return CommonResponse.<T>builder()
                .statusCode(code)
                .message(message)
                .data(data)
                .build();
    }

    @PutMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CommonResponse<MerchantResponse>> updateMerchantById(@RequestBody MerchantRequest merchantRequest){
        MerchantResponse merchantResponse = merchantService.updateMerchantById(merchantRequest);
        CommonResponse<MerchantResponse> response = generateToCustomerResponse(HttpStatus.OK.value(), "Success update merchant with Id " + merchantRequest.getId(), Optional.of(merchantResponse));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{merchantId}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CommonResponse<String>> deleteMerchantById(@PathVariable String merchantId){
        merchantService.deleteById(merchantId);
        return ResponseEntity.ok().body(generateToCustomerResponse(HttpStatus.OK.value(), "Success delete merchant with Id " + merchantId, Optional.empty()));
    }

    @GetMapping("/{merchantId}")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<MerchantResponse>> getMerchantById(@PathVariable String merchantId){
        MerchantResponse merchantResponse = merchantService.getMerchantById(merchantId);
        CommonResponse<MerchantResponse> response = generateToCustomerResponse(HttpStatus.FOUND.value(), "Success get Merchant with id " + merchantId, Optional.of(merchantResponse));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/allmerchants")
//    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<List<MerchantResponse>>> getAllMerchant(){
        List<MerchantResponse> merchantResponseList = merchantService.getAll();
        CommonResponse<List<MerchantResponse>> response = generateToCustomerResponse(HttpStatus.FOUND.value(), "Success get all merchants", Optional.of(merchantResponseList));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{merchantId}/customers")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getCustomersWhoBoughtFromMerchant(@PathVariable String merchantId){
        List<CustomerResponse> customerResponseList = merchantService.getCustomersWhoBoughtFromMerchant(merchantId);
        CommonResponse<List<CustomerResponse>> response = generateToCustomerResponse(HttpStatus.OK.value(), "Success get customers who bought from merchant with id " + merchantId, Optional.of(customerResponseList));
        return ResponseEntity.ok().body(response);
    }
}
