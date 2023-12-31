package com.example.orderserver.order.controller;

import com.example.orderserver.dto.RequestOrderDto;
import com.example.orderserver.dto.ResponseMessageDto;
import com.example.orderserver.member.LoginMember;
import com.example.orderserver.messagequeue.KafkaProducer;
import com.example.orderserver.order.service.OrderService;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;

    public OrderController(OrderService orderService, KafkaProducer kafkaProducer) {
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/products/{productId}")
    @Timed(value = "orders.payment", longTask = true)
    public ResponseEntity<ResponseMessageDto> payment(@PathVariable Long productId,
                                     @RequestBody RequestOrderDto requestDto,
                                     @LoginMember Long memberId) throws ExecutionException, InterruptedException {

        ResponseMessageDto payment = orderService.payment(productId, requestDto, memberId);

        if (payment.getStatusCode() == 200) {
            kafkaProducer.sendProductId(productId);
        }

        return ResponseEntity.status(payment.getStatusCode()).body(payment);
    }
}
