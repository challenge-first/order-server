package com.example.orderserver.dto;

import lombok.Getter;

@Getter
public class RequestProductDto {
    private Long productId;
    public RequestProductDto(Long productId) {
        this.productId = productId;
    }
}
