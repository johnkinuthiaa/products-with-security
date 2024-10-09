package com.products.products.repository;

import com.products.products.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Products,Long> {
    Optional<Products> findByProductName(String name);
}
