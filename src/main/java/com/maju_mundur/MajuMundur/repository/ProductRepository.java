package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Merchant;
import com.maju_mundur.MajuMundur.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findAllByMerchant(Merchant merchant);
}
