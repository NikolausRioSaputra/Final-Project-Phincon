package com.nikolaus.reactivePayment.controller;


import com.nikolaus.common.dto.BalanceDTO;
import com.nikolaus.reactivePayment.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BalanceDTO>> getBalanceById(@PathVariable Long id) {
        return balanceService.getBalanceById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<BalanceDTO>> createOrUpdateBalance(@RequestBody BalanceDTO balanceDTO) {
        return balanceService.createOrUpdateBalance(balanceDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
