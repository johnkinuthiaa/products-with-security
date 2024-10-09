package com.products.products.service;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;

import java.util.List;

public interface ProductServiceInterface {
    List<Products> getAllProducts();
    ProductResponse createNewProduct(Products products);
}
