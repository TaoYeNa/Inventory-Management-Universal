package com.Ecommerceservice.orderservice.exception;

import com.Ecommerceservice.orderservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,null,ex.getMessage()));
//    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<ApiResponse> handleInsufficientInventory(InsufficientInventoryException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ApiResponse(false,null,ex.getMessage()));
    }


}

