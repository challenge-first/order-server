package com.example.orderserver.client;

import com.example.orderserver.dto.ResponseStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-server")
public interface ProductServiceClient {
    @GetMapping("/products/stock/{productId}")
    ResponseEntity<ResponseStockDto> getStock(@PathVariable Long productId);
}
