package com.foodmarket.repository;

import com.foodmarket.model.entity.ItemQuantityInStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<ItemQuantityInStockEntity, Long> {}
