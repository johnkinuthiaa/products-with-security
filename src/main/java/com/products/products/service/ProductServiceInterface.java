package com.products.products.service;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;

import java.util.List;

public interface ProductServiceInterface {
    ProductResponse  getAllProducts();
    ProductResponse createNewProduct(Products products);
    ProductResponse updateExistingProduct(Products products,Long id);
    ProductResponse getProductsByName(String name);
    ProductResponse getProductsByCategory(String category);

}
