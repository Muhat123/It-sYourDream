package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.CustomerResponse;
import com.maju_mundur.MajuMundur.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    private <T> CommonResponse<T> generateToCustomerResponse(Integer code, String message, Optional<T> data) {
        return CommonResponse.<T>builder()
                .statusCode(code)
                .message(message)
                .data(data)
                .build();
    }

    private CommonResponse<String> generateMessageResponse(Integer code, String message) {
        return CommonResponse.<String>builder()
                .statusCode(code)
                .message(message)
                .data(Optional.of(message))
                .build();
    }

    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomerById(@RequestBody CustomerRequest customerRequest){
        CustomerResponse response = customerService.updateCustomerById(customerRequest);
        CommonResponse<CustomerResponse> commonResponse = generateToCustomerResponse(HttpStatus.OK.value(),"Success update customer with id " + customerRequest.getId(), Optional.of(response));
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String customerId){
        CustomerResponse response = customerService.getCustomerById(customerId);
        CommonResponse<CustomerResponse> commonResponse = generateToCustomerResponse(HttpStatus.OK.value(),"Success get customer with id " + customerId, Optional.of(response));
        return ResponseEntity.ok(commonResponse);
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<String>> deleteCustomerById(@PathVariable String customerId){
        String deletedCustomer = customerService.deleteById(customerId);
        CommonResponse<String> commonResponse = generateMessageResponse(HttpStatus.OK.value(),"Success delete customer with id " + customerId);
        return ResponseEntity.ok(commonResponse);
    }

    @GetMapping("/allcustomers")
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers(){
        List<CustomerResponse> customerResponse = customerService.getAll();
        CommonResponse<List<CustomerResponse>> commonResponse = generateToCustomerResponse(HttpStatus.OK.value(),"Success get all customers", Optional.of(customerResponse));
        return ResponseEntity.ok(commonResponse);
    }
}
