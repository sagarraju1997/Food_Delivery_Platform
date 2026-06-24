package com.food_delivery_platform.restaurant_service.service;

import com.food_delivery_platform.restaurant_service.dto.MenuItemRequest;
import com.food_delivery_platform.restaurant_service.dto.MenuItemResponse;
import com.food_delivery_platform.restaurant_service.entity.MenuItem;
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
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemResponse addMenuItem(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found or inactive"));

        // Check if menu item with same name already exists for this restaurant
        if (menuItemRepository.existsByRestaurantIdAndName(restaurantId, request.getName())) {
            throw new RuntimeException("Menu item with this name already exists for this restaurant");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        menuItem.setIsVegetarian(request.getIsVegetarian() != null ? request.getIsVegetarian() : false);
        menuItem.setIsVegan(request.getIsVegan() != null ? request.getIsVegan() : false);
        menuItem.setAllergens(request.getAllergens());
        menuItem.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        menuItem.setRestaurant(restaurant);

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return convertToResponse(savedMenuItem);
    }

    public MenuItemResponse updateMenuItem(Long restaurantId, Long menuItemId, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new RuntimeException("Menu item not found for this restaurant"));

        // Check if another menu item with same name exists (excluding current item)
        if (!menuItem.getName().equals(request.getName()) && 
            menuItemRepository.existsByRestaurantIdAndName(restaurantId, request.getName())) {
            throw new RuntimeException("Menu item with this name already exists for this restaurant");
        }

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : menuItem.getIsAvailable());
        menuItem.setIsVegetarian(request.getIsVegetarian() != null ? request.getIsVegetarian() : menuItem.getIsVegetarian());
        menuItem.setIsVegan(request.getIsVegan() != null ? request.getIsVegan() : menuItem.getIsVegan());
        menuItem.setAllergens(request.getAllergens());
        menuItem.setPreparationTimeMinutes(request.getPreparationTimeMinutes());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return convertToResponse(updatedMenuItem);
    }

    public void deleteMenuItem(Long restaurantId, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new RuntimeException("Menu item not found for this restaurant"));
        
        menuItemRepository.delete(menuItem);
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByCategory(Long restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategoryAndIsAvailableTrue(restaurantId, category)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItem(Long restaurantId, Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new RuntimeException("Menu item not found for this restaurant"));
        return convertToResponse(menuItem);
    }

    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setCategory(menuItem.getCategory());
        response.setImageUrl(menuItem.getImageUrl());
        response.setIsAvailable(menuItem.getIsAvailable());
        response.setIsVegetarian(menuItem.getIsVegetarian());
        response.setIsVegan(menuItem.getIsVegan());
        response.setAllergens(menuItem.getAllergens());
        response.setPreparationTimeMinutes(menuItem.getPreparationTimeMinutes());
        response.setRestaurantId(menuItem.getRestaurant().getId());
        response.setRestaurantName(menuItem.getRestaurant().getName());
        response.setCreatedAt(menuItem.getCreatedAt());
        response.setUpdatedAt(menuItem.getUpdatedAt());
        return response;
    }
}