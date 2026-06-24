package com.food_delivery_platform.restaurant_service.repository;

import com.food_delivery_platform.restaurant_service.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndCategory(Long restaurantId, String category);

    List<MenuItem> findByRestaurantIdAndCategoryAndIsAvailableTrue(Long restaurantId, String category);

    Optional<MenuItem> findByIdAndRestaurantId(Long id, Long restaurantId);

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.name LIKE %:name% AND m.isAvailable = true")
    List<MenuItem> findByRestaurantIdAndNameContainingIgnoreCaseAndIsAvailableTrue(@Param("restaurantId") Long restaurantId, @Param("name") String name);

    @Query("SELECT DISTINCT m.category FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.isAvailable = true")
    List<String> findDistinctCategoriesByRestaurantId(@Param("restaurantId") Long restaurantId);

    List<MenuItem> findByRestaurantIdAndIsVegetarianTrueAndIsAvailableTrue(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndIsVeganTrueAndIsAvailableTrue(Long restaurantId);

    boolean existsByRestaurantIdAndName(Long restaurantId, String name);

    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND m.isAvailable = true")
    long countAvailableItemsByRestaurantId(@Param("restaurantId") Long restaurantId);
}