package com.example.demo.dto.product.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.product.ProductResponse;
import com.example.demo.models.Product;

@Component 
public class ProductMapper {

    public ProductResponse toResponse(Product product){
        return  ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .sku(product.getSku())
        .description(product.getDescription())
        .imageUrl(product.getImageUrl())
        .regularPrice(product.getRegularPrice())
        .offerPrice(product.getOfferPrice())
        .initialStock(product.getInitialStock())
        .minStockAlert(product.getMinStockAlert())
        .categoryId(product.getCategory() != null ? product.getCategory().getId():null)
        .categoryName(product.getCategory() != null ? product.getCategory().getName():null)
        .createdAt(product.getCreatedAt())
        .updatedAt(product.getUpdatedAt())
        .build();
    }
    
}
