package com.nikolaus.reactivePayment.service;

import com.nikolaus.common.dto.BalanceDTO;
import com.nikolaus.common.dto.TransactionDetailsDTO;
import com.nikolaus.reactivePayment.model.TransactionDetails;
import com.nikolaus.reactivePayment.repository.BalanceRepository;
import com.nikolaus.reactivePayment.repository.TransactionDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    public Mono<TransactionDetailsDTO> debit(BalanceDTO balanceDTO) {
        return balanceRepository.findById(balanceDTO.getId())
                .flatMap(balance -> {
                    if (balance.getAmount() >= balanceDTO.getAmount()) {
                        balance.setAmount(balance.getAmount() - balanceDTO.getAmount());
                        return balanceRepository.save(balance)
                                .then(createTransaction(balanceDTO, "debit", "approved"));
                    } else {
                        return createTransaction(balanceDTO, "debit", "rejected");
                    }
                });
    }

    public Mono<TransactionDetailsDTO> credit(BalanceDTO balanceDTO) {
        return balanceRepository.findById(balanceDTO.getId())
                .flatMap(balance -> {
                    balance.setAmount(balance.getAmount() + balanceDTO.getAmount());
                    return balanceRepository.save(balance)
                            .then(createTransaction(balanceDTO, "credit", "approved"));
                });
    }

    private Mono<TransactionDetailsDTO> createTransaction(BalanceDTO balanceDTO, String mode, String status) {
        TransactionDetails transactionDetails = TransactionDetails.builder()
                .amount(balanceDTO.getAmount())
                .orderId(balanceDTO.getId())
                .paymentDate(new Timestamp(System.currentTimeMillis()))
                .mode(mode)
                .status(status)
                .referenceNumber("REF-" + System.currentTimeMillis())
                .build();

        return transactionDetailsRepository.save(transactionDetails)
                .map(this::mapTransactionToDTO);
    }

    private TransactionDetailsDTO mapTransactionToDTO(TransactionDetails transactionDetails) {
        return TransactionDetailsDTO.builder()
                .id(transactionDetails.getId())
                .amount(transactionDetails.getAmount())
                .orderId(transactionDetails.getOrderId())
                .paymentDate(transactionDetails.getPaymentDate())
                .mode(transactionDetails.getMode())
                .status(transactionDetails.getStatus())
                .referenceNumber(transactionDetails.getReferenceNumber())
                .build();
    }
}
