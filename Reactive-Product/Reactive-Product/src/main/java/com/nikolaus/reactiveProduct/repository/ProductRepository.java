package com.nikolaus.reactiveProduct.repository;

import com.nikolaus.reactiveProduct.model.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<Product, Long> {
    Mono<Product> findById(Long id);
}
