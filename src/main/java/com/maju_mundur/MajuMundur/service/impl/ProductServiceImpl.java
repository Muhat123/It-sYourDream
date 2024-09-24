package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.request.ProductRequest;
import com.maju_mundur.MajuMundur.dto.response.ProductResponse;
import com.maju_mundur.MajuMundur.entity.Product;
import com.maju_mundur.MajuMundur.repository.ProductRepository;  // Pastikan kamu memiliki repository ini
import com.maju_mundur.MajuMundur.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository; // Ganti dengan repository yang sesuai

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        // Membuat entitas Product dari request
        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .build();

        // Menyimpan produk ke dalam database
        Product savedProduct = productRepository.saveAndFlush(newProduct);

        // Mengembalikan response
        return mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAll() {
        // Mengambil semua produk dari database
        List<Product> products = productRepository.findAll();

        // Mengubah ke List<ProductResponse>
        return products.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(String id) {
        // Mencari produk berdasarkan ID
        Optional<Product> productOpt = productRepository.findById(id);

        // Jika ditemukan, kembalikan sebagai response
        return productOpt.map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void deleteById(String id) {
        // Mencari dan menghapus produk berdasarkan ID
        Optional<Product> productOpt = productRepository.findById(id);
        productOpt.ifPresent(productRepository::delete);
    }

    @Override
    public void updateStatusById(String id) {
        // Mencari produk berdasarkan ID dan memperbarui status atau atribut lainnya
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            // Contoh: mengubah atribut produk jika perlu
            product.setPrice(product.getPrice() + 10.0); // Misalnya, menambah harga produk
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    // Helper method untuk mengubah Product entity menjadi ProductResponse DTO
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
