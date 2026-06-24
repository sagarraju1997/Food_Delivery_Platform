package com.food_delivery_platform.restaurant_service.repository;

import com.food_delivery_platform.restaurant_service.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOwnerId(Long ownerId);

    List<Restaurant> findByIsActiveTrue();

    List<Restaurant> findByOwnerIdAndIsActiveTrue(Long ownerId);

    Optional<Restaurant> findByIdAndIsActiveTrue(Long id);

    @Query("SELECT r FROM Restaurant r WHERE r.name LIKE %:name% AND r.isActive = true")
    List<Restaurant> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);

    @Query("SELECT r FROM Restaurant r WHERE r.address LIKE %:address% AND r.isActive = true")
    List<Restaurant> findByAddressContainingIgnoreCaseAndIsActiveTrue(@Param("address") String address);

    boolean existsByOwnerIdAndName(Long ownerId, String name);
}