package com.Ecommerceservice.orderservice.service;

import com.Ecommerceservice.orderservice.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Receiver {
    private final ObjectMapper objectMapper;
    private final AIModelService aiModelService;
    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    public void receiveAIMessage(String message){
        try{
            List<Order> orders = objectMapper.readValue(message, objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class));
            List<Order> response = aiModelService.simulateExternalAPICall(orders);
            log.info("Processed Order Message: Received " + response.size() + " orders after simulated API call");
        } catch (JsonMappingException e) {
            log.error("Error when Mapping Json" + e.getMessage());

        } catch (JsonProcessingException e) {
            log.error("Error when processing Json" + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("Error Happened in RunTime for receiveAIMessage" + e.getMessage());
        }

    }

    public void receiveTestMessage(String message){
        try{
            log.info("this is a test message from RabbitMQ" + message);
        }
        catch(Exception e){
            log.error("Error occurs when testing" + e.getMessage());
        }
    }
}
