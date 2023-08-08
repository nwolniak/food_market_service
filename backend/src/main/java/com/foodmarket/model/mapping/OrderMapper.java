package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.OrderResponseDto;
import com.foodmarket.model.dto.OrderResponseDto.ItemQuantity;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "orderedItems", target = "orderItems", qualifiedByName = "orderedItemsMapping")
    OrderResponseDto orderEntityToDto(OrderEntity orderEntity);

    @Named("orderedItemsMapping")
    default List<ItemQuantity> orderedItemsMapping(List<OrderItemEntity> orderedProducts) {
        return orderedProducts
                .stream()
                .map(this::orderItemEntityToDto)
                .toList();
    }

    @Mapping(source = "itemEntity.id", target = "itemId")
    ItemQuantity orderItemEntityToDto(OrderItemEntity orderItemEntity);

}
