package com.nikolaus.reactivePayment.service;

import com.nikolaus.common.dto.BalanceDTO;
import com.nikolaus.reactivePayment.model.Balance;
import com.nikolaus.reactivePayment.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public Mono<BalanceDTO> getBalanceById(Long id) {
        return balanceRepository.findById(id)
                .map(this::mapToDTO);
    }

    public Mono<BalanceDTO> createOrUpdateBalance(BalanceDTO balanceDTO) {
        return balanceRepository.save(mapToEntity(balanceDTO))
                .map(this::mapToDTO);
    }

    private BalanceDTO mapToDTO(Balance balance) {
        return BalanceDTO.builder()
                .id(balance.getId())
                .amount(balance.getAmount())
                .customerId(balance.getCustomerId())
                .build();
    }

    private Balance mapToEntity(BalanceDTO balanceDTO) {
        return Balance.builder()
                .id(balanceDTO.getId())
                .amount(balanceDTO.getAmount())
                .customerId(balanceDTO.getCustomerId())
                .build();
    }
}
