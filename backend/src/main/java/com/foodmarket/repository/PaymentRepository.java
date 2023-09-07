package com.foodmarket.repository;

import com.foodmarket.model.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Stream<PaymentEntity> findByUserEntity_Id(Long id);

}