package com.example.orderserver.order.service;

import com.example.orderserver.dto.RequestOrderDto;
import com.example.orderserver.dto.ResponseMessageDto;

public interface OrderService {
    ResponseMessageDto payment(Long productId, RequestOrderDto requestDto, Long memberId);
}
