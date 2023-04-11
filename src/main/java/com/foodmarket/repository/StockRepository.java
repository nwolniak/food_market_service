package com.foodmarket.repository;

import com.foodmarket.model.entity.ProductCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<ProductCountEntity, Long> {

    @Modifying
    @Query("update ProductCountEntity pc set pc.quantityInStock = pc.quantityInStock + 1 where pc.id= :productId")
    int incrementCounter(@Param("productId") Long productId);

}
