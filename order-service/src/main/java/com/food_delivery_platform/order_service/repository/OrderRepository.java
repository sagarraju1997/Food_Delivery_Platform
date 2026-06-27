package com.food_delivery_platform.order_service.repository;

import com.food_delivery_platform.order_service.entity.OrderEntity;
import com.food_delivery_platform.order_service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndStatus(Long userId, OrderStatus status);
}
