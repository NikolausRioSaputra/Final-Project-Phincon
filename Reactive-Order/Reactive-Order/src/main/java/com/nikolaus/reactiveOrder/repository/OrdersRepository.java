package com.nikolaus.reactiveOrder.repository;

import com.nikolaus.reactiveOrder.model.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrdersRepository extends R2dbcRepository<Order, Long> {

}
