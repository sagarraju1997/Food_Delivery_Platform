package com.food_delivery_platform.cart_service.controller;

import com.food_delivery_platform.cart_service.dto.CartItemRequest;
import com.food_delivery_platform.cart_service.dto.CartItemResponse;
import com.food_delivery_platform.cart_service.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // For simplicity, accept userId as a header. In production, extract from JWT.
    private Long resolveUserId(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.isBlank()) {
            throw new IllegalArgumentException("X-User-Id header is required");
        }
        return Long.parseLong(userIdHeader);
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addOrUpdateItem(@RequestHeader("X-User-Id") String userIdHeader,
                                                            @Valid @RequestBody CartItemRequest request) {
        Long userId = resolveUserId(userIdHeader);
        return ResponseEntity.ok(cartService.addOrUpdateItem(userId, request));
    }

    @PatchMapping("/items/{menuItemId}")
    public ResponseEntity<CartItemResponse> incrementItem(@RequestHeader("X-User-Id") String userIdHeader,
                                                          @PathVariable Long menuItemId,
                                                          @RequestParam(name = "delta", defaultValue = "1") int delta) {
        Long userId = resolveUserId(userIdHeader);
        return ResponseEntity.ok(cartService.incrementItem(userId, menuItemId, delta));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestHeader("X-User-Id") String userIdHeader) {
        Long userId = resolveUserId(userIdHeader);
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<Void> removeItem(@RequestHeader("X-User-Id") String userIdHeader,
                                           @PathVariable Long menuItemId) {
        Long userId = resolveUserId(userIdHeader);
        cartService.removeItem(userId, menuItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-Id") String userIdHeader) {
        Long userId = resolveUserId(userIdHeader);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
