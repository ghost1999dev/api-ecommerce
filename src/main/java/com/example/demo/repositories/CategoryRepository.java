package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category>findByName(String name);
    List<Category> findTop5ByOrderByIdAsc();
    
}
