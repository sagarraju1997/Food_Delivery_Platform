package com.food_delivery_platform.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    @NotNull
    private Long restaurantId;
    @NotNull
    private List<Item> items;

    @Data
    public static class Item {
        @NotNull
        private Long menuItemId;
        @NotNull
        private String name;
        @NotNull
        private java.math.BigDecimal price;
        @Min(1)
        private int quantity;
    }
}
