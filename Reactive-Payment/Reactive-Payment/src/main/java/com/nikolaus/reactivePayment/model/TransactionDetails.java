package com.nikolaus.reactivePayment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("transaction_details")
public class TransactionDetails {

    @Id
    private Long id;
    private double amount;
    private Long orderId;
    private Timestamp paymentDate;
    private String mode;
    private String status;
    private String referenceNumber;
}
