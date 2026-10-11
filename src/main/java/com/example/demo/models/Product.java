package com.example.demo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity 
@Table (name = "products")
public class Product {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false, length = 200)
    private String name;
    @Column(length = 100)
    private String sku;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "regular_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal regularPrice;
    @Column(name = "offer_price", precision = 10, scale = 2)
    private BigDecimal offerPrice;
    @Column(name = "initial_stick", nullable = false)
    private Integer initialStock;
    @Column(name = "min_stock_alert")
    private Integer minStockAlert;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "category_id", nullable = false)
    private Category category;
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    
}
