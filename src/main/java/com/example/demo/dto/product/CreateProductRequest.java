package com.example.demo.dto.product;

import java.math.BigDecimal;

import lombok.Data;
@Data 
public class CreateProductRequest {

    private String name;
    private Long categoryId;
    private String sku;
    private String description;
    private BigDecimal regularPrice;
    private BigDecimal offerPrice;
    private Integer initialStock;
    private Integer minStockAlert;
    
}
