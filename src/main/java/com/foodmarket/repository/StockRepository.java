package com.foodmarket.repository;

import com.foodmarket.model.entity.ProductCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<ProductCountEntity, Long> {}
