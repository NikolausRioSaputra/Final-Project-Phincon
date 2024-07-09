package com.nikolaus.reactiveOrder.service;

import com.nikolaus.common.dto.OrderDTO;
import com.nikolaus.common.dto.OrderItemDTO;
import com.nikolaus.common.enums.OrderStatus;
import com.nikolaus.reactiveOrder.model.Order;
import com.nikolaus.reactiveOrder.model.OrderItem;
import com.nikolaus.reactiveOrder.repository.OrderItemRepository;
import com.nikolaus.reactiveOrder.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    private static final String ORDER_TOPIC = "order-events";

    public Flux<OrderDTO> getAllOrders() {
        return ordersRepository.findAll()
                .flatMap(this::mapOrderToOrderDTO);
    }

    public Mono<OrderDTO> createOrder(OrderDTO orderDTO) {
        orderDTO.setOrderStatus(OrderStatus.CREATED.name());
        return mapOrderDTOToOrder(orderDTO)
                .flatMap(order -> ordersRepository.save(order)
                        .flatMap(savedOrder -> {
                            List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                                    .map(item -> {
                                        OrderItem orderItem = mapOrderItemDTOToOrderItem(item);
                                        orderItem.setOrderId(savedOrder.getId());
                                        return orderItem;
                                    }).collect(Collectors.toList());
                            return orderItemRepository.saveAll(orderItems).collectList()
                                    .thenReturn(savedOrder);
                        })
                        .flatMap(savedOrder -> mapOrderToOrderDTO(savedOrder)
                                .doOnSuccess(dto -> kafkaTemplate.send(ORDER_TOPIC, dto))));
    }

    private Mono<OrderDTO> mapOrderToOrderDTO(Order order) {
        return orderItemRepository.findByOrderId(order.getId())
                .collectList()
                .map(orderItems -> OrderDTO.builder()
                        .id(order.getId())
                        .billingAddress(order.getBilingAddress())
                        .customerId(order.getCustomerId())
                        .orderDate(order.getOrderDate())
                        .orderStatus(order.getOrderStatus())
                        .paymentMethod(order.getPaymentMethod())
                        .shippingAddress(order.getShippingAddress())
                        .totalAmount(order.getTotalAmount())
                        .orderItems(orderItems.stream()
                                .map(this::mapOrderItemToOrderItemDTO)
                                .collect(Collectors.toList()))
                        .build());
    }

    private OrderItemDTO mapOrderItemToOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .price(orderItem.getPrice())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .orderId(orderItem.getOrderId())
                .build();
    }

    private Mono<Order> mapOrderDTOToOrder(OrderDTO orderDTO) {
        return Mono.just(Order.builder()
                .bilingAddress(orderDTO.getBillingAddress())
                .customerId(orderDTO.getCustomerId())
                .orderDate(orderDTO.getOrderDate())
                .orderStatus(orderDTO.getOrderStatus())
                .paymentMethod(orderDTO.getPaymentMethod())
                .shippingAddress(orderDTO.getShippingAddress())
                .totalAmount(orderDTO.getTotalAmount())
                .build());
    }

    private OrderItem mapOrderItemDTOToOrderItem(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .price(orderItemDTO.getPrice())
                .productId(orderItemDTO.getProductId())
                .quantity(orderItemDTO.getQuantity())
                .orderId(orderItemDTO.getOrderId())
                .build();
    }

    @KafkaListener(topics = "order-update-events", groupId = "niko-group")
    public void updateOrderStatus(OrderDTO orderDTO) {
        ordersRepository.findById(orderDTO.getId())
                .flatMap(order -> {
                    order.setOrderStatus(orderDTO.getOrderStatus());
                    return ordersRepository.save(order);
                })
                .subscribe();
    }
}
