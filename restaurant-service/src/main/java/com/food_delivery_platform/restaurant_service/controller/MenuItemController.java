package com.food_delivery_platform.restaurant_service.controller;

import com.food_delivery_platform.restaurant_service.dto.MenuItemRequest;
import com.food_delivery_platform.restaurant_service.dto.MenuItemResponse;
import com.food_delivery_platform.restaurant_service.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> addMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request) {
        try {
            MenuItemResponse response = menuItemService.addMenuItem(restaurantId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId,
            @Valid @RequestBody MenuItemRequest request) {
        try {
            MenuItemResponse response = menuItemService.updateMenuItem(restaurantId, menuItemId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId) {
        try {
            menuItemService.deleteMenuItem(restaurantId, menuItemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getMenuItems(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String category) {
        try {
            List<MenuItemResponse> menuItems;
            if (category != null && !category.isEmpty()) {
                menuItems = menuItemService.getMenuItemsByCategory(restaurantId, category);
            } else {
                menuItems = menuItemService.getMenuItemsByRestaurant(restaurantId);
            }
            return ResponseEntity.ok(menuItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> getMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId) {
        try {
            MenuItemResponse response = menuItemService.getMenuItem(restaurantId, menuItemId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}