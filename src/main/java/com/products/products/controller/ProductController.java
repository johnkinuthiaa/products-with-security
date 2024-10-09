package com.products.products.controller;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;
import com.products.products.service.ProductServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductServiceInterface service;
    public ProductController(ProductServiceInterface service){
        this.service=service;
    }
    @GetMapping("/home")
    public ResponseEntity<?> homepage(){
        return ResponseEntity.ok("please log in to access the app");
    }
    @PostMapping("/admin/new/product")
    public ResponseEntity<ProductResponse> createNewProduct(@RequestBody Products products){
        return ResponseEntity.ok(service.createNewProduct(products));
    }

}
