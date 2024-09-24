package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.constant.ApiUrl;
import com.maju_mundur.MajuMundur.dto.request.MerchantRequest;
import com.maju_mundur.MajuMundur.dto.response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.response.MerchantResponse;
import com.maju_mundur.MajuMundur.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiUrl.MERCHANT)
public class MerchantController {
    private final MerchantService merchantService;

    // Create Merchant
    @PostMapping
    public ResponseEntity<CommonResponse<MerchantResponse>> createMerchant(@RequestBody MerchantRequest merchantRequest) {
        MerchantResponse merchantResponse = merchantService.create(merchantRequest);
        CommonResponse<MerchantResponse> commonResponse = CommonResponse.<MerchantResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Merchant created successfully!")
                .data(merchantResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    // Get All Merchants
    @GetMapping
    public ResponseEntity<CommonResponse<List<MerchantResponse>>> getAllMerchants() {
        List<MerchantResponse> merchants = merchantService.getAll();
        CommonResponse<List<MerchantResponse>> commonResponse = CommonResponse.<List<MerchantResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("List of merchants")
                .data(merchants)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Get Merchant by ID
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<MerchantResponse>> getMerchantById(@PathVariable String id) {
        MerchantResponse merchantResponse = merchantService.getMerchantById(id);
        CommonResponse<MerchantResponse> commonResponse = CommonResponse.<MerchantResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Merchant found")
                .data(merchantResponse)
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Update Merchant by ID
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<MerchantResponse>> updateMerchantById(@PathVariable String id, @RequestBody MerchantRequest merchantRequest) {
        merchantService.updateById(id, merchantRequest);
        CommonResponse<MerchantResponse> commonResponse = CommonResponse.<MerchantResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Merchant updated successfully")
                .build();
        return ResponseEntity.ok(commonResponse);
    }

    // Delete Merchant by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteMerchantById(@PathVariable String id) {
        merchantService.deleteById(id);
        CommonResponse<Void> commonResponse = CommonResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Merchant deleted successfully")
                .build();
        return ResponseEntity.ok(commonResponse);
    }
}
