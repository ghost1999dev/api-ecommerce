package com.example.demo.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data 
@Builder 
public class ProductResponse {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private String imageUrl;
    private BigDecimal regularPrice;
    private BigDecimal offerPrice;
    private Integer initialStock;
    private Integer minStockAlert;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
