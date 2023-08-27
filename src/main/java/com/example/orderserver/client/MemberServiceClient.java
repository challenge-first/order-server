package com.example.orderserver.client;

import com.example.orderserver.dto.RequestOrderDto;
import com.example.orderserver.dto.ResponsePointDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "member-server")
public interface MemberServiceClient {
    @GetMapping("/members/point")
    ResponseEntity<ResponsePointDto> getPoint(@RequestHeader("x-authorization-id") Long memberId);

    @PutMapping("/members/point")
    ResponseEntity<ResponsePointDto> payPoint(@RequestBody RequestOrderDto requestDto,
                                              @RequestHeader("x-authorization-id") Long memberId);
}
