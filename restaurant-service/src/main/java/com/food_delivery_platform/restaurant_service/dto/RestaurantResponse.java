package com.food_delivery_platform.restaurant_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
    private Long ownerId;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long menuItemCount;

    // Explicit setter for menuItemCount to ensure it's generated
    public void setMenuItemCount(Long menuItemCount) {
        this.menuItemCount = menuItemCount;
    }
}
