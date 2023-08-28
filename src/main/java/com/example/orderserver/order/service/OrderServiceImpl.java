package com.example.orderserver.order.service;

import com.example.orderserver.client.MemberServiceClient;
import com.example.orderserver.client.ProductServiceClient;
import com.example.orderserver.dto.RequestOrderDto;
import com.example.orderserver.dto.ResponseMessageDto;
import com.example.orderserver.dto.ResponsePointDto;
import com.example.orderserver.dto.ResponseStockDto;
import com.example.orderserver.entity.Order;
import com.example.orderserver.entity.OrderStatus;
import com.example.orderserver.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.orderserver.entity.OrderStatus.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberServiceClient memberServiceClient;
    private final ProductServiceClient productServiceClient;

    public OrderServiceImpl(OrderRepository orderRepository, MemberServiceClient memberServiceClient, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.memberServiceClient = memberServiceClient;
        this.productServiceClient = productServiceClient;
    }

    @Override
    @Transactional
    public ResponseMessageDto payment(Long productId, RequestOrderDto requestDto, Long memberId) {
        ResponsePointDto pointDto = memberServiceClient.getPoint(memberId).getBody();
        ResponseStockDto productDto = productServiceClient.getStock(productId).getBody();
        try {
            assert pointDto != null;
            assert productDto != null;

            pointNotEnough(pointDto.getAvailablePoint(), requestDto.getPrice());
            hasStock(productDto.getStockCount());

            memberServiceClient.payPoint(requestDto, memberId);

        } catch (Exception e) {
            saveOrder(memberId, productId, FAIL, requestDto.getPrice());
            return new ResponseMessageDto(e.getMessage(), HttpStatus.PAYMENT_REQUIRED.value(), HttpStatus.PAYMENT_REQUIRED.getReasonPhrase());
        }
        saveOrder(memberId, productId, SUCCESS, requestDto.getPrice());
        return new ResponseMessageDto("결제 완료", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    private Order saveOrder(Long memberId, Long productId, OrderStatus status, Long price) {
        Order order = Order.builder()
                .memberId(memberId)
                .productId(productId)
                .price(price)
                .orderStatus(status)
                .build();

        return orderRepository.save(order);
    }

    private void pointNotEnough(Long point, Long price) {
        if (point < price) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

    private void hasStock(Long stock) {
        if (stock <= 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
    }
}
