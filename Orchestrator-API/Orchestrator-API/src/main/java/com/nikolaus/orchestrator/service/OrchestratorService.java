package com.nikolaus.orchestrator.service;

import com.nikolaus.common.dto.OrderDTO;
import com.nikolaus.common.dto.BalanceDTO;
import com.nikolaus.common.dto.TransactionDetailsDTO;
import com.nikolaus.common.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@Slf4j
public class OrchestratorService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private KafkaTemplate<String, OrderDTO> orderKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, TransactionDetailsDTO> transactionKafkaTemplate;

    private static final String ORDER_UPDATE_TOPIC = "order-update-events";
    private static final String PAYMENT_STATUS_TOPIC = "payment-status-events";

    @KafkaListener(topics = "order-events", groupId = "niko-group")
    public void consumeOrderEvent(OrderDTO orderDTO) {
        // Logging request body
        log.info("Sending request to /product/deduct: " + orderDTO);

        webClientBuilder.build()
                .post()
                .uri("http://localhost:8081/product/deduct")
                .body(Mono.just(orderDTO), OrderDTO.class)
                .retrieve()
                .bodyToMono(OrderDTO.class)
                .flatMap(updatedOrderDTO -> {
                    if (OrderStatus.COMPLETED.name().equals(updatedOrderDTO.getOrderStatus())) {
                        log.info("ini isi message yang udah di update "+ updatedOrderDTO);
                        BalanceDTO balanceDTO = BalanceDTO.builder()
                                .id(updatedOrderDTO.getCustomerId())
                                .amount(updatedOrderDTO.getTotalAmount())
                                .customerId(updatedOrderDTO.getCustomerId())
                                .build();
                        orderKafkaTemplate.send(ORDER_UPDATE_TOPIC, updatedOrderDTO);

                        return webClientBuilder.build()
                                .post()
                                .uri("http://localhost:8083/payment/debit")
                                .body(Mono.just(balanceDTO), BalanceDTO.class)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .then(Mono.just(updatedOrderDTO))
                                .doOnSuccess(o -> {
                                    TransactionDetailsDTO transactionDetailsDTO = TransactionDetailsDTO.builder()
                                            .orderId(updatedOrderDTO.getId())
                                            .amount(updatedOrderDTO.getTotalAmount())
                                            .paymentDate(new Timestamp(System.currentTimeMillis()))
                                            .mode("DEBIT")
                                            .status("APPROVED")
                                            .referenceNumber(UUID.randomUUID().toString())
                                            .build();

                                    transactionKafkaTemplate.send(PAYMENT_STATUS_TOPIC, transactionDetailsDTO);
                                })
                                .doOnError(error -> {
                                    updatedOrderDTO.setOrderStatus(OrderStatus.FAILED.name());
                                    orderKafkaTemplate.send(ORDER_UPDATE_TOPIC, updatedOrderDTO);
                                });
                    } else {
                        return Mono.just(updatedOrderDTO)
                                .doOnSuccess(o -> orderKafkaTemplate.send(ORDER_UPDATE_TOPIC, updatedOrderDTO))
                                .doOnError(error -> {
                                    updatedOrderDTO.setOrderStatus(OrderStatus.FAILED.name());
                                    orderKafkaTemplate.send(ORDER_UPDATE_TOPIC, updatedOrderDTO);
                                });
                    }
                })
                .subscribe();
    }

}
