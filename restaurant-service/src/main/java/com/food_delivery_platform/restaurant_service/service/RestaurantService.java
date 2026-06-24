package com.food_delivery_platform.restaurant_service.service;

import com.food_delivery_platform.restaurant_service.dto.RestaurantRequest;
import com.food_delivery_platform.restaurant_service.dto.RestaurantResponse;
import com.food_delivery_platform.restaurant_service.entity.Restaurant;
import com.food_delivery_platform.restaurant_service.repository.MenuItemRepository;
import com.food_delivery_platform.restaurant_service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        // Check if restaurant with same name already exists for this owner
        if (restaurantRepository.existsByOwnerIdAndName(request.getOwnerId(), request.getName())) {
            throw new RuntimeException("Restaurant with this name already exists for this owner");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setEmail(request.getEmail());
        restaurant.setDescription(request.getDescription());
        restaurant.setOwnerId(request.getOwnerId());
        restaurant.setIsActive(true);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToResponse(savedRestaurant);
    }

    public RestaurantResponse updateRestaurant(Long restaurantId, RestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found or inactive"));

        // Check if another restaurant with same name exists for this owner (excluding current restaurant)
        if (!restaurant.getName().equals(request.getName()) && 
            restaurantRepository.existsByOwnerIdAndName(request.getOwnerId(), request.getName())) {
            throw new RuntimeException("Restaurant with this name already exists for this owner");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setEmail(request.getEmail());
        restaurant.setDescription(request.getDescription());

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return convertToResponse(updatedRestaurant);
    }

    public void deleteRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found or inactive"));

        // Soft delete - mark as inactive instead of hard delete
        restaurant.setIsActive(false);
        restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found or inactive"));
        return convertToResponse(restaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurantsByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerIdAndIsActiveTrue(ownerId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllActiveRestaurants() {
        return restaurantRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> searchRestaurantsByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private RestaurantResponse convertToResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setAddress(restaurant.getAddress());
        response.setPhoneNumber(restaurant.getPhoneNumber());
        response.setEmail(restaurant.getEmail());
        response.setDescription(restaurant.getDescription());
        response.setOwnerId(restaurant.getOwnerId());
        response.setIsActive(restaurant.getIsActive());
        response.setCreatedAt(restaurant.getCreatedAt());
        response.setUpdatedAt(restaurant.getUpdatedAt());

        // Get menu item count
        long menuItemCount = menuItemRepository.countAvailableItemsByRestaurantId(restaurant.getId());
        response.setMenuItemCount(Long.valueOf(menuItemCount));

        return response;
    }
}
