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
public class BalanceDTO {
    @NotNull(message = "ID tidak boleh kosong")
    private Long id;

    @NotNull(message = "Amount tidak boleh kosong")
    private double amount;

    @NotNull(message = "Customer ID tidak boleh kosong")
    private Long customerId;
}
