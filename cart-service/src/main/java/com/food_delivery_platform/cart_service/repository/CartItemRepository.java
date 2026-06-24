package com.food_delivery_platform.cart_service.repository;

import com.food_delivery_platform.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserIdAndMenuItemId(Long userId, Long menuItemId);
    List<CartItem> findByUserId(Long userId);
    void deleteByUserIdAndMenuItemId(Long userId, Long menuItemId);
    void deleteByUserId(Long userId);
}
