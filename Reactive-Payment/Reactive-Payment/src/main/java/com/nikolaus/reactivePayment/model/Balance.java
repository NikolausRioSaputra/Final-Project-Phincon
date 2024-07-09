package com.nikolaus.reactivePayment.model;

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
@Table("balance")
public class Balance {
    @Id
    private Long id;
    private double amount;
    private Long customerId;
}
