package com.nikolaus.reactiveProduct.controller;


import com.nikolaus.common.dto.OrderDTO;
import com.nikolaus.common.dto.ProductDTO;
import com.nikolaus.reactiveProduct.model.Product;
import com.nikolaus.reactiveProduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PostMapping("/deduct")
    public Mono<ResponseEntity<OrderDTO>> deductProduct(@RequestBody OrderDTO orderDTO) {
        return productService.deductProduct(orderDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<Void>> addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO)
                .thenReturn(ResponseEntity.ok().<Void>build());
    }
}
