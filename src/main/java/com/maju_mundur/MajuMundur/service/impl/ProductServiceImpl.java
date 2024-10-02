package com.maju_mundur.MajuMundur.service.impl;

import com.maju_mundur.MajuMundur.dto.Request.ProductRequest;
import com.maju_mundur.MajuMundur.dto.Response.ImageResponse;
import com.maju_mundur.MajuMundur.dto.Response.ProductResponse;
import com.maju_mundur.MajuMundur.entity.Image;
import com.maju_mundur.MajuMundur.entity.Merchant;
import com.maju_mundur.MajuMundur.entity.Product;
import com.maju_mundur.MajuMundur.entity.User;
import com.maju_mundur.MajuMundur.exception.OurException;
import com.maju_mundur.MajuMundur.repository.ImageRepository;
import com.maju_mundur.MajuMundur.repository.MerchantRepository;
import com.maju_mundur.MajuMundur.repository.ProductRepository;
import com.maju_mundur.MajuMundur.service.FileStorageService;
import com.maju_mundur.MajuMundur.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .stock(product.getQuantity())
                .merchantId(product.getMerchant().getId())
                .build();
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantRepository.findByUserId(loggedInUser.getId());

        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .quantity(productRequest.getQuantity())
                .merchant(merchant)
                .build();
        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new OurException("Product tidak ditemukan"));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getMyProduct() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantRepository.findByUserId(loggedInUser.getId());
        if (merchant == null) {
            throw new OurException("Merchant tidak ditemukan");
        }
        List<Product> productList = productRepository.findAllByMerchant(merchant);
        return productList.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ProductResponse> getAllProductsByMerchant(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            throw new OurException("Merchant ID tidak boleh kosong");
        }
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new OurException("Merchant tidak ditemukan"));
        List<Product> productList = productRepository.findAllByMerchant(merchant);
        return productList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    @Override //only for admin
    public List<ProductResponse> getAllProducts() {
        List<Product> listProductOnlyForAdmin = productRepository.findAll();
        return listProductOnlyForAdmin.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override //admin and merchant
    public ProductResponse updateProduct(ProductRequest productRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantRepository.findByUserId(loggedInUser.getId());

        Product product = productRepository.findById(productRequest.getId()).orElseThrow(() -> new OurException("Produk tidak ditemukan"));
        if (!productRequest.getName().isBlank()) {
            product.setName(productRequest.getName());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (!productRequest.getDescription().isBlank()) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getQuantity() > 0) {
            product.setQuantity(productRequest.getQuantity());
        }
        product.setMerchant(merchant);

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);

    }


    @Override
    public String deleteProduct(String productId) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Merchant merchant = merchantRepository.findByUserId(loggedInUser.getId());

        Product product = productRepository.findById(productId).orElseThrow(() -> new OurException("Produk tidak ditemukan"));
        if (product.getMerchant().getId().equals(merchant.getId())) {
            productRepository.deleteById(productId);
            return "Produk berhasil dihapus";
        } else {
            return "Anda tidak memiliki akses untuk menghapus produk ini";
        }
    }

    @Transactional
    @Override
    public ImageResponse uploadPoster(MultipartFile file, String productId){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productRepository.findById(productId).orElseThrow(() -> new OurException("Product tidak ditemukan"));


        String fileName = fileStorageService.storeFile(file, loggedInUser.getId());
//        List<Image> oldPoster = product.getProductPoster();
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/product")
                .path("/poster/")
                .path(fileName)
                .toUriString();

        Image poster = Image.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .path(fileDownloadUri)
                .product(product)
                .build();

        imageRepository.save(poster);
        product.getProductPoster().add(poster);
        productRepository.save(product);

        return ImageResponse.builder()
                .name(poster.getName())
                .size(file.getSize())
                .contentType(poster.getContentType())
                .path(poster.getPath())
                .build();
    }

    @Transactional
    @Override
    public void deletePoster(String posterId) {
        Image poster = imageRepository.findById(posterId).orElseThrow(() -> new OurException("Poster tidak ditemukan"));
        imageRepository.delete(poster);
    }
}
