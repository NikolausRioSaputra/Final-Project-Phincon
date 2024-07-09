package com.nikolaus.common.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    @NotNull(message = "Price cannot be null")
    private float price;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "Order ID cannot be null")
    private Long orderId;
}
