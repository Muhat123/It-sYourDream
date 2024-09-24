package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.constant.ApiUrl;
import com.maju_mundur.MajuMundur.dto.request.CustomerRequest;
import com.maju_mundur.MajuMundur.dto.response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.response.CustomerResponse;
import com.maju_mundur.MajuMundur.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.CUSTOMER)
public class CustomerController {
    private final CustomerService customerService;

    // Create Customer
    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = customerService.create(customerRequest);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Customer created successfully!")
                .data(customerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    // Get All Customers
    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAll();
        CommonResponse<List<CustomerResponse>> commonResponse = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("List of customers")
                .data(customers)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getCustomerById(id);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer found")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Update Customer by ID
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomerById(@PathVariable String id, @RequestBody CustomerRequest customerRequest) {
        customerService.updateById(id, customerRequest);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer updated successfully")
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Delete Customer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteCustomerById(@PathVariable String id) {
        customerService.deleteById(id);
        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer deleted successfully")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
}
