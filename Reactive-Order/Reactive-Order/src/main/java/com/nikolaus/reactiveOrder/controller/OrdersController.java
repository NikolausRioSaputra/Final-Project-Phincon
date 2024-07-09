package com.nikolaus.reactiveOrder.controller;

import com.nikolaus.common.dto.OrderDTO;
import com.nikolaus.reactiveOrder.service.OrdersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public Flux<OrderDTO> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @PostMapping
    public Mono<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ordersService.createOrder(orderDTO);
    }
}
