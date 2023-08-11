package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.PaymentDto;
import com.foodmarket.model.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PaymentMapper {

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "orderEntity.id", target = "orderId")
    PaymentDto toDto(PaymentEntity paymentEntity);

    PaymentEntity toEntity(PaymentDto paymentDto);

}