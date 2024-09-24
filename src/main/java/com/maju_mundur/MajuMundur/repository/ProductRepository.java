package com.maju_mundur.MajuMundur.repository;

import com.maju_mundur.MajuMundur.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
