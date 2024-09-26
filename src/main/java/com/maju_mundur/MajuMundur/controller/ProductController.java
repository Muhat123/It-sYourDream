package com.maju_mundur.MajuMundur.controller;

import com.maju_mundur.MajuMundur.dto.Request.ProductRequest;
import com.maju_mundur.MajuMundur.dto.Response.CommonResponse;
import com.maju_mundur.MajuMundur.dto.Response.ProductResponse;
import com.maju_mundur.MajuMundur.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private CommonResponse<ProductResponse> generateProductResponse(Integer code, String message, Optional<ProductResponse> productResponse) {
        return CommonResponse.<ProductResponse>builder()
                .statusCode(code)
                .message(message)
                .data(productResponse)
                .build();
    }

    private CommonResponse<List<ProductResponse>> generateProductResponseList(Integer code, String message, Optional<List<ProductResponse>> productResponse) {
        return CommonResponse.<List<ProductResponse>>builder()
                .statusCode(code)
                .message(message)
                .data(productResponse)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        CommonResponse<ProductResponse> response = generateProductResponse(HttpStatus.CREATED.value(), "Success Add Product", Optional.of(productResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String productId) {
        ProductResponse productResponse = productService.getProductById(productId);
        CommonResponse<ProductResponse> response = generateProductResponse(HttpStatus.OK.value(), "Success get product with id : " + productId, Optional.of(productResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/myproducts")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getMyProduct() {
        List<ProductResponse> productResponseList = productService.getMyProduct();
        CommonResponse<List<ProductResponse>> response = generateProductResponseList(HttpStatus.OK.value(), "Success get My Products", Optional.of(productResponseList));
        return ResponseEntity.ok(response);
    }

    @GetMapping("myproducts/{merchantId}")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProductsByMerchantId(@PathVariable String merchantId){
        List<ProductResponse> productResponseList = productService.getAllProductsByMerchant(merchantId);
        CommonResponse<List<ProductResponse>> response = generateProductResponseList(HttpStatus.OK.value(), "Success get All products by merchant", Optional.of(productResponseList));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allproducts")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProducts(){
        List<ProductResponse> productResponseList = productService.getAllProducts();
        CommonResponse<List<ProductResponse>> response = generateProductResponseList(HttpStatus.OK.value(), "Success get All Products", Optional.of(productResponseList));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(@PathVariable String productId, @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(productRequest);
        CommonResponse<ProductResponse> response = generateProductResponse(HttpStatus.OK.value(), "Success update product with id : " + productId, Optional.of(productResponse));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<String> deleteProduct(@PathVariable String productId){
        String product = productService.deleteProduct(productId);
        return ResponseEntity.ok(product);
    }
}
