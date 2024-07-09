package com.nikolaus.reactivePayment.repository;

import com.nikolaus.reactivePayment.model.TransactionDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;


public interface TransactionDetailsRepository extends R2dbcRepository<TransactionDetails, Long> {
}
