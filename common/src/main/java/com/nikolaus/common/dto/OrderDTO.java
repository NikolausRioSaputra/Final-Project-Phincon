package com.nikolaus.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    @NotNull(message = "id address cannot be null")
    private Long id;

    @NotNull(message = "Billing address cannot be null")
    private String billingAddress;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Order date cannot be null")
    private LocalDate orderDate;

    @NotNull(message = "Order status cannot be null")
    private String orderStatus;

    @NotBlank(message = "Payment method cannot be null or blank")
    private String paymentMethod;

    @NotBlank(message = "Shipping address cannot be null or blank")
    private String shippingAddress;

    @NotNull(message = "Total amount cannot be null")
    private float totalAmount;

    private List<OrderItemDTO> orderItems;

}
