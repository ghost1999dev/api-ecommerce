package com.example.demo.dto.category.mappper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.category.CategoryResponse;
import com.example.demo.models.Category;

@Component 
public class CategoryMapper {
    public CategoryResponse toResponse(Category category){
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .description(category.getDescription())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();
        
    }
}
