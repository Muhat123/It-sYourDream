package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.request.ProductRequest;
import com.maju_mundur.MajuMundur.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest product);

    List<ProductResponse> getAll();

    ProductResponse getProductById(String id);

    void deleteById(String id);

    void updateStatusById(String id);
}
