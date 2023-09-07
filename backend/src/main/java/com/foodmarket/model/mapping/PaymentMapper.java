package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.PaymentDto;
import com.foodmarket.model.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "orderEntity.id", target = "orderId")
    PaymentDto toDto(PaymentEntity paymentEntity);

    PaymentEntity toEntity(PaymentDto paymentDto);

}