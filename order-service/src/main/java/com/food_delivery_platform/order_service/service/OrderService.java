package com.food_delivery_platform.order_service.service;

import com.food_delivery_platform.order_service.dto.OrderResponse;
import com.food_delivery_platform.order_service.dto.PlaceOrderRequest;
import com.food_delivery_platform.order_service.entity.OrderEntity;
import com.food_delivery_platform.order_service.entity.OrderItem;
import com.food_delivery_platform.order_service.entity.OrderStatus;
import com.food_delivery_platform.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse placeOrder(Long userId, PlaceOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        BigDecimal total = request.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = OrderEntity.builder()
                .userId(userId)
                .restaurantId(request.getRestaurantId())
                .status(OrderStatus.PENDING)
                .totalAmount(total)
                .build();

        List<OrderItem> items = request.getItems().stream().map(i -> OrderItem.builder()
                .order(order)
                .menuItemId(i.getMenuItemId())
                .name(i.getName())
                .price(i.getPrice())
                .quantity(i.getQuantity())
                .build()).collect(Collectors.toList());
        order.setItems(items);

        OrderEntity saved = orderRepository.save(order);
        return toResponse(saved);
    }

    public List<OrderResponse> getOrderHistory(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("Not allowed");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot cancel after confirmation");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private OrderResponse toResponse(OrderEntity e) {
        return OrderResponse.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .restaurantId(e.getRestaurantId())
                .status(e.getStatus())
                .totalAmount(e.getTotalAmount())
                .createdAt(e.getCreatedAt())
                .items(e.getItems().stream().map(it -> OrderResponse.Item.builder()
                        .menuItemId(it.getMenuItemId())
                        .name(it.getName())
                        .price(it.getPrice())
                        .quantity(it.getQuantity())
                        .build()).toList())
                .build();
    }
}
