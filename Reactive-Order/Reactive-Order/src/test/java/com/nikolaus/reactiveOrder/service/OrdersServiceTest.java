package com.nikolaus.reactiveOrder.service;



import com.nikolaus.common.dto.OrderDTO;
import com.nikolaus.reactiveOrder.model.Order;
import com.nikolaus.reactiveOrder.model.OrderItem;
import com.nikolaus.reactiveOrder.repository.OrderItemRepository;
import com.nikolaus.reactiveOrder.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import static org.mockito.Mockito.when;

public class OrdersServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    @InjectMocks
    private OrdersService ordersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllOrders() {
        Order order = Order.builder()
                .id(1L)
                .bilingAddress("123 Billing St")
                .customerId(101L)
                .orderDate(LocalDate.now())
                .orderStatus("Processing")
                .paymentMethod("Credit Card")
                .shippingAddress("123 Shipping St")
                .totalAmount(150.0f)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .price(100.0f)
                .productId(1L)
                .quantity(1)
                .orderId(1L)
                .build();

        when(ordersRepository.findAll()).thenReturn(Flux.just(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(Flux.just(orderItem));

        Flux<OrderDTO> orderDTOFlux = ordersService.getAllOrders();

        StepVerifier.create(orderDTOFlux)
                .expectNextMatches(orderDTO -> orderDTO.getId().equals(order.getId()))
                .verifyComplete();
    }

}