package com.products.products.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.products.products.model.Products;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private int statusCode;
    private String error;
    private String message;
    private LocalDate createdOn;
    private LocalDate deletedOn;
    private String name;
    private Products products;
    private List<Products> allProducts;

}
