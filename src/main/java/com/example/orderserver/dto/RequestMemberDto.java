package com.example.orderserver.dto;

import lombok.Getter;

@Getter
public class RequestMemberDto {
    private Long memberId;
    private Long price;

    public RequestMemberDto(Long id, Long price) {
        this.memberId = id;
        this.price = price;
    }
}
