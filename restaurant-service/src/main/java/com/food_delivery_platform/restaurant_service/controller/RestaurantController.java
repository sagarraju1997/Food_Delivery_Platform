package com.food_delivery_platform.restaurant_service.controller;

import com.food_delivery_platform.restaurant_service.dto.RestaurantRequest;
import com.food_delivery_platform.restaurant_service.dto.RestaurantResponse;
import com.food_delivery_platform.restaurant_service.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest request) {
        try {
            RestaurantResponse response = restaurantService.createRestaurant(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long restaurantId,
            @Valid @RequestBody RestaurantRequest request) {
        try {
            RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long restaurantId) {
        try {
            restaurantService.deleteRestaurant(restaurantId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long restaurantId) {
        try {
            RestaurantResponse response = restaurantService.getRestaurant(restaurantId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurants(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String name) {
        try {
            List<RestaurantResponse> restaurants;
            if (ownerId != null) {
                restaurants = restaurantService.getRestaurantsByOwner(ownerId);
            } else if (name != null && !name.isEmpty()) {
                restaurants = restaurantService.searchRestaurantsByName(name);
            } else {
                restaurants = restaurantService.getAllActiveRestaurants();
            }
            return ResponseEntity.ok(restaurants);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}