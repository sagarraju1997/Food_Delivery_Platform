package com.food_delivery_platform.order_service.controller;

import com.food_delivery_platform.order_service.dto.OrderResponse;
import com.food_delivery_platform.order_service.dto.PlaceOrderRequest;
import com.food_delivery_platform.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private Long resolveUserId(String header) {
        if (header == null || header.isBlank()) {
            throw new IllegalArgumentException("X-User-Id header is required");
        }
        return Long.parseLong(header);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("X-User-Id") String userHeader,
                                                    @Valid @RequestBody PlaceOrderRequest request) {
        Long userId = resolveUserId(userHeader);
        OrderResponse response = orderService.placeOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> orderHistory(@RequestHeader("X-User-Id") String userHeader) {
        Long userId = resolveUserId(userHeader);
        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancel(@RequestHeader("X-User-Id") String userHeader,
                                       @PathVariable Long orderId) {
        Long userId = resolveUserId(userHeader);
        orderService.cancelOrder(userId, orderId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Bad request";
        HttpStatus status = msg.toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormat(NumberFormatException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid X-User-Id header");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Operation not allowed";
        HttpStatus status = msg.toLowerCase().contains("cannot cancel") ? HttpStatus.CONFLICT : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(msg);
    }
}
