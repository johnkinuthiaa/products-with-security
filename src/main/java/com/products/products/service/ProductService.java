package com.products.products.service;

import com.products.products.dto.ProductResponse;
import com.products.products.model.Products;
import com.products.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductServiceInterface{
    private final ProductRepository repository;
    public ProductService(ProductRepository repository){
        this.repository=repository;
    }

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public  ProductResponse getAllProducts(){

        ProductResponse response =new ProductResponse();
        try {
            List <Products> result= repository.findAll();
            if(!result.isEmpty()){
                    response.setStatusCode(200);
                    response.setMessage("all the products");
                    response.setAllProducts(result);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
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
                response.setCreatedOn(LocalDate.parse(dtf.format(LocalDate.now())));
                response.setName(productResult.getProductName());
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }
    @Override
    public ProductResponse updateExistingProduct(Products products,Long id){
        ProductResponse response =new ProductResponse();
        try {
            if(repository.findProductsById(id).isPresent()){
                repository.deleteById(id);
                Products product =new Products();
                product.setId(id);
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
                    response.setCreatedOn(LocalDate.parse(dtf.format(LocalDate.now())));
                    response.setName(productResult.getProductName());
                }

            }else {
                response.setMessage("product with id"+id+"was not found");
                response.setStatusCode(500);
            }
        }catch(Exception e){
            response.setMessage("product with id"+id+"was not found");
            response.setStatusCode(500);

        }
        return response;

    }
    @Override
    public ProductResponse getProductsByName(String name){
        ProductResponse response =new ProductResponse();
        try{
            List<Products> product =repository.findAll().stream()
                    .filter(products -> products.getProductName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
            if(!product.isEmpty()){
                response.setStatusCode(200);
                response.setMessage("object with name "+name+" was found");
                response.setAllProducts(product);
                response.setName(name);
            }else {
                response.setStatusCode(500);
                response.setError("object not found");
            }
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }
    @Override
    public ProductResponse getProductsByCategory(String category){
        ProductResponse response =new ProductResponse();
        try {
            List<Products> productsCategory =repository.findAll().stream()
                    .filter(products -> products.getCategory().toLowerCase().contains(category.toLowerCase()))
                    .collect(Collectors.toList());
            if(!productsCategory.isEmpty()){
                response.setStatusCode(200);
                response.setName(category);
                response.setMessage("products found with "+category+" category");

                response.setAllProducts(productsCategory);
            }else {
                response.setStatusCode(500);
                response.setMessage("no item with "+category+" was found");
            }

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("no item with "+category+" was found");
        }
        return response;
    }
    @Override
    public ProductResponse deleteProductsById(Long id){
        ProductResponse response =new ProductResponse();
        try {
            if(repository.findById(id).isPresent()){
                Products products;
                products =repository.getById(id);
                repository.deleteById(id);
                response.setStatusCode(200);
                response.setMessage("successfully deleted the product with id "+id+".");
                response.setName(response.getName());
                response.setProducts(products);
                response.setDeletedOn(LocalDate.parse(dtf.format(LocalDate.now())));
            }else {
                response.setStatusCode(500);
                response.setMessage("item with id "+id+" was not found");
            }
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("item with id "+id+" was not found");
        }
        return response;
    }
    @Override
    public ProductResponse deleteAllProducts(){
        ProductResponse response =new ProductResponse();
        try{
            if(!repository.findAll().isEmpty()){
                repository.deleteAll();
                response.setStatusCode(200);
                response.setMessage("Successfully deleted all items");
                response.setDeletedOn(LocalDate.parse(dtf.format(LocalDate.now())));

            }else{
                response.setStatusCode(500);
                response.setMessage("the product list is already empty");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
























