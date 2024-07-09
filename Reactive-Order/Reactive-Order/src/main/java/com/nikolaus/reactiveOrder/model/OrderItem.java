package com.nikolaus.reactiveOrder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_item")
public class OrderItem {

    @Id
    private Long id;
    private float price;
    private Long productId;
    private Integer quantity;
    private Long orderId;

}
