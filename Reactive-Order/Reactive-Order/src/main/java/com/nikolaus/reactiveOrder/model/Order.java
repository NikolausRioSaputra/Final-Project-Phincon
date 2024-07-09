package com.nikolaus.reactiveOrder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("orders")
public class Order {

    @Id
    private Long id;
    private String bilingAddress;
    private Long customerId;
    private LocalDate orderDate;
    private String orderStatus;
    private String paymentMethod;
    private String shippingAddress;
    private float totalAmount;

}
