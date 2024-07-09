package com.nikolaus.reactivePayment.controller;

import com.nikolaus.common.dto.BalanceDTO;
import com.nikolaus.common.dto.TransactionDetailsDTO;
import com.nikolaus.reactivePayment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/debit")
    public Mono<ResponseEntity<TransactionDetailsDTO>> debit(@RequestBody BalanceDTO balanceDTO) {
        return paymentService.debit(balanceDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/credit")
    public Mono<ResponseEntity<TransactionDetailsDTO>> credit(@RequestBody BalanceDTO balanceDTO) {
        return paymentService.credit(balanceDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
