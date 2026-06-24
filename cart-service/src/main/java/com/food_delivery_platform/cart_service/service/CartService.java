package com.food_delivery_platform.cart_service.service;

import com.food_delivery_platform.cart_service.dto.CartItemRequest;
import com.food_delivery_platform.cart_service.dto.CartItemResponse;
import com.food_delivery_platform.cart_service.entity.CartItem;
import com.food_delivery_platform.cart_service.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;

    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartItemResponse addOrUpdateItem(Long userId, CartItemRequest request) {
        CartItem item = cartItemRepository.findByUserIdAndMenuItemId(userId, request.getMenuItemId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUserId(userId);
                    ci.setMenuItemId(request.getMenuItemId());
                    ci.setQuantity(0);
                    return ci;
                });
        item.setQuantity(request.getQuantity());
        CartItem saved = cartItemRepository.save(item);
        return new CartItemResponse(saved.getId(), saved.getMenuItemId(), saved.getQuantity());
    }

    @Transactional
    public CartItemResponse incrementItem(Long userId, Long menuItemId, int delta) {
        if (delta == 0) delta = 1;
        CartItem item = cartItemRepository.findByUserIdAndMenuItemId(userId, menuItemId)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUserId(userId);
                    ci.setMenuItemId(menuItemId);
                    ci.setQuantity(0);
                    return ci;
                });
        int newQty = Math.max(0, (item.getQuantity() == null ? 0 : item.getQuantity()) + delta);
        if (newQty == 0 && item.getId() != null) {
            cartItemRepository.deleteByUserIdAndMenuItemId(userId, menuItemId);
            return new CartItemResponse(null, menuItemId, 0);
        }
        item.setQuantity(newQty);
        CartItem saved = cartItemRepository.save(item);
        return new CartItemResponse(saved.getId(), saved.getMenuItemId(), saved.getQuantity());
    }

    public List<CartItemResponse> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId)
                .stream()
                .map(ci -> new CartItemResponse(ci.getId(), ci.getMenuItemId(), ci.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeItem(Long userId, Long menuItemId) {
        cartItemRepository.deleteByUserIdAndMenuItemId(userId, menuItemId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
