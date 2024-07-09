package com.nikolaus.reactiveProduct.service;

import com.nikolaus.common.dto.OrderDTO;
import com.nikolaus.common.dto.ProductDTO;
import com.nikolaus.common.enums.OrderStatus;
import com.nikolaus.reactiveProduct.model.Product;
import com.nikolaus.reactiveProduct.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, ProductDTO> kafkaTemplate;

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Mono<OrderDTO> deductProduct(OrderDTO orderDTO) {
        Integer quantity = orderDTO.getOrderItems().get(0).getQuantity();

        return productRepository.findById(orderDTO.getOrderItems().get(0).getProductId())
                .flatMap(product -> {
                    if (product.getStockQuantity() >= quantity) {
                        product.setStockQuantity(product.getStockQuantity() - quantity);
                        orderDTO.setOrderStatus(OrderStatus.COMPLETED.name());

                        orderDTO.getOrderItems().get(0).setPrice(product.getPrice());
                        orderDTO.setTotalAmount(product.getPrice() * quantity);
                        log.info("status ini apa " + orderDTO.getOrderStatus());
                        log.info("total amount" + orderDTO.getTotalAmount());
                        log.info("data apa dsisni " + orderDTO );
                        return productRepository.save(product).thenReturn(orderDTO);
                    } else {
                        orderDTO.setOrderStatus(OrderStatus.FAILED.name());
                        return Mono.just(orderDTO);
                    }
                });
    }

    public Mono<Void> addProduct(ProductDTO productDTO) {
        return productRepository.findById(productDTO.getId())
                .flatMap(product -> {
                    product.setStockQuantity(product.getStockQuantity() + productDTO.getStockQuantity());
                    product.setUpdatedAt(LocalDateTime.now());
                    return productRepository.save(product).then();
                });
    }
}
