package com.example.orderserver.dto;

import lombok.Getter;

@Getter
public class ResponsePointDto {

    private Long point;
    private Long deposit;
    private Long availablePoint;
}