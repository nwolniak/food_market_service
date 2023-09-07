package com.foodmarket.repository;

import com.foodmarket.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Stream<OrderEntity> findByUserEntity_Id(Long id);

}
