package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.ProductRequest;
import com.maju_mundur.MajuMundur.dto.Response.ImageResponse;
import com.maju_mundur.MajuMundur.dto.Response.ProductResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse getProductById(String productId);

    List<ProductResponse> getMyProduct();

    List<ProductResponse> getAllProductsByMerchant(String merchantId);

    //admin only
    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(ProductRequest productRequest);

    String deleteProduct(String productId);

    ImageResponse uploadPoster(MultipartFile file, String productId);

    @Transactional
    void deletePoster(String posterId);
}
