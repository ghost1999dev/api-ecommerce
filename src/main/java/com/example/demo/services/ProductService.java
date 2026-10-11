package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.product.CreateProductRequest;
import com.example.demo.dto.product.ProductResponse;
import com.example.demo.dto.product.mapper.ProductMapper;
import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

     private static final String DEFAULT_PRODUCT_IMAGE = "https://images.unsplash.com/photo-1542291026-7eec264c27ff";
    
    @Transactional 
    public ProductResponse createProduct(CreateProductRequest request){
        if(request.getName()== null || request.getName().trim().isEmpty()){
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        if(request.getCategoryId() == null){
            throw new RuntimeException("La categoria es obligatoria");

        }
        if(request.getRegularPrice() == null){
            throw new RuntimeException("El precio regular es obligatorio");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(()-> new RuntimeException("Categoria no encontrada con id: " + request.getCategoryId()));
        
        Product product =Product.builder()
            .name(request.getName().trim())
            .sku(request.getSku())
            .description(request.getDescription())
            .imageUrl(DEFAULT_PRODUCT_IMAGE)
            .regularPrice(request.getRegularPrice())
            .offerPrice(request.getOfferPrice())
            .initialStock(request.getInitialStock() !=null ? request.getInitialStock():0)
            .minStockAlert(request.getMinStockAlert() !=null ? request.getMinStockAlert():0)
            .category(category)
            .build();
        Product savedProdut = productRepository.save(product);
        return productMapper.toResponse(savedProdut);
    }

    @Transactional 
    public List<ProductResponse> getProductResponses(Long categoryId){
        if(!categoryRepository.existsById(categoryId)){
            throw new RuntimeException("Categoria no encontrada con id: " + categoryId);
        }
        return  productRepository.findByCategoryIdOrderByIdDesc(categoryId)
        .stream()
        .map(productMapper::toResponse)
        .collect(Collectors.toList());
    }
    
}
