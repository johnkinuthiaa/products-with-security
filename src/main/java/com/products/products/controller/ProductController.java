package com.products.products.controller;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;
import com.products.products.service.ProductServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api/v1/products")
@RequestMapping()
public class ProductController {
    private final ProductServiceInterface service;
    public ProductController(ProductServiceInterface service){
        this.service=service;
    }
    @GetMapping("/home")
    public ResponseEntity<?> homepage(){
        return ResponseEntity.ok("please log in to access the app");
    }
    @GetMapping("/all")
    public  ResponseEntity<ProductResponse> getAllProducts(){
        return ResponseEntity.ok(service.getAllProducts());
    }
    @GetMapping("/get/name")
    public ResponseEntity<ProductResponse> getProductsByName(@RequestParam String name){
        return ResponseEntity.ok(service.getProductsByName(name));
    }
    @GetMapping("/get/category")
    public  ResponseEntity<ProductResponse> getProductsByCategory(@RequestParam String category){
        return ResponseEntity.ok(service.getProductsByCategory(category));
    }
    @PostMapping("/admin/new/product")
    public ResponseEntity<ProductResponse> createNewProduct(@RequestBody Products products){
        return ResponseEntity.ok(service.createNewProduct(products));
    }
    @PutMapping("/update/product")
    public ResponseEntity<ProductResponse> updateExistingProduct(@RequestBody Products products,@RequestParam Long id){
        return ResponseEntity.ok(service.updateExistingProduct(products,id));
    }
    @DeleteMapping("/delete/id")
    public ResponseEntity<ProductResponse> deleteProductsById(@RequestParam Long id){
        return ResponseEntity.ok(service.deleteProductsById(id));
    }
    @DeleteMapping("/all/products")
    public ResponseEntity<ProductResponse> deleteAllProducts(){
        return ResponseEntity.ok(service.deleteAllProducts());
    }

}
