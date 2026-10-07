package com.example.demo.services;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.category.CategoryResponse;
import com.example.demo.dto.category.CreateCategoryRequest;
import com.example.demo.dto.category.mappper.CategoryMapper;
import com.example.demo.models.Category;
import com.example.demo.repositories.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional 
    public CategoryResponse createCategory(CreateCategoryRequest request){
        if(request.getName() == null || request.getName().trim().isEmpty()){
            throw new RuntimeException("El nombre de la categoria es obligatorio");
        }
        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);

    }

    public List<CategoryResponse>getTop5Categories(){
        return categoryRepository.findTop5ByOrderByIdAsc()
            .stream()
            .map(categoryMapper::toResponse)
            .collect(Collectors.toList());
    }
    
}
