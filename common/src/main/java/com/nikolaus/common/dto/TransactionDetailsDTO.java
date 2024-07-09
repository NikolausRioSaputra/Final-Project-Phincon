package com.nikolaus.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailsDTO {

    @NotNull(message = "ID tidak boleh kosong")
    private Long id;

    @NotNull(message = "Amount tidak boleh kosong")
    private double amount;

    @NotNull(message = "Order ID tidak boleh kosong")
    private Long orderId;

    @NotNull(message = "Payment date tidak boleh kosong")
    private Timestamp paymentDate;

    @NotBlank(message = "Mode tidak boleh kosong")
    private String mode;

    @NotBlank(message = "Status tidak boleh kosong")
    private String status;

    @NotBlank(message = "Reference number tidak boleh kosong")
    private String referenceNumber;
}
