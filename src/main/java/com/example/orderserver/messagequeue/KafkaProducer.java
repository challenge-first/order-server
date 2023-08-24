package com.example.orderserver.messagequeue;

import com.example.orderserver.dto.RequestMemberDto;
import com.example.orderserver.dto.RequestProductDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.producer.topic.product}")
    private String stockTopic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPrice(String topic, Long memberId, Long price) throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(new RequestMemberDto(memberId, price));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        SendResult<String, String> stringStringSendResult = kafkaTemplate.send(topic, jsonInString).get();
    }

    public void sendProductId(Long productId) throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(new RequestProductDto(productId));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(stockTopic, jsonInString);
    }
}
