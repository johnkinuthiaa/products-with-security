package com.products.products.service;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;
import com.products.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductServiceInterface{
    private final ProductRepository repository;
    public ProductService(ProductRepository repository){
        this.repository=repository;
    }

    @Override
    public List<Products> getAllProducts(){
       return repository.findAll();
    }

    @Override
    public ProductResponse createNewProduct(Products products){
        ProductResponse response =new ProductResponse();
        try{
            Products product =new Products();
            product.setProductName(products.getProductName());
            product.setImgUrl(products.getImgUrl());
            product.setPrice(products.getPrice());
            product.setDescription(products.getDescription());
            product.setManufacturer(products.getManufacturer());
            product.setQuantity(products.getQuantity());
            product.setCategory(products.getCategory());
            product.setStatus(products.getStatus());
            Products productResult =repository.save(product);

            if(productResult !=null &&productResult.getId()>0){
                response.setProducts(productResult);
                response.setMessage("new product created successfully successfully");
                response.setStatusCode(200);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }


        return response;
    }
}
