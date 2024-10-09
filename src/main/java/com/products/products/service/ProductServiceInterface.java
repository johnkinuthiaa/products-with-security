package com.products.products.service;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;

import java.util.List;

public interface ProductServiceInterface {
    ProductResponse  getAllProducts();
    ProductResponse createNewProduct(Products products);
}
