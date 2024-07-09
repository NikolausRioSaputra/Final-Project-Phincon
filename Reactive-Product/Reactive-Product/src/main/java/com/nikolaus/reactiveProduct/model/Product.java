package com.nikolaus.reactiveProduct.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("product")
public class Product {

    @Id
    private Long id;
    private String name;
    private float price;
    private String category;
    private LocalDateTime createdAt;
    private String description;
    private String imageUrl;
    private Integer stockQuantity;
    private LocalDateTime updatedAt;

}
