package com.foodmarket.repository;

import com.foodmarket.model.entity.ProductCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<ProductCountEntity, Long> {}
