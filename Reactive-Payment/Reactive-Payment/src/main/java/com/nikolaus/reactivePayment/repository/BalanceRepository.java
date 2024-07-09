package com.nikolaus.reactivePayment.repository;

import com.nikolaus.reactivePayment.model.Balance;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BalanceRepository extends R2dbcRepository<Balance, Long> {
}
