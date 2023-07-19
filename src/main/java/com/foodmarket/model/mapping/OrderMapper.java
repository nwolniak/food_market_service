package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.OrderDTO.ItemQuantity;
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

    @Mapping(source = "orderedItems", target = "orderedItems", qualifiedByName = "orderedItemsMapping")
    OrderDTO orderEntityToDto(OrderEntity orderEntity);

    @Named("orderedItemsMapping")
    default List<ItemQuantity> map(List<OrderItemEntity> orderedProducts) {
        return orderedProducts
                .stream()
                .map(this::orderItemEntityToDto)
                .toList();
    }

    @Mapping(source = "itemEntity.id", target = "id")
    ItemQuantity orderItemEntityToDto(OrderItemEntity orderItemEntity);

}
