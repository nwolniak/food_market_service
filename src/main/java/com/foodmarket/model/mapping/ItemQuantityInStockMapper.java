package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ItemQuantityInStockDTO;
import com.foodmarket.model.entity.ItemQuantityInStockEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemQuantityInStockMapper {

    ItemQuantityInStockMapper INSTANCE = Mappers.getMapper(ItemQuantityInStockMapper.class);

    ItemQuantityInStockDTO itemQuantityInStockToDto(ItemQuantityInStockEntity itemQuantityInStockEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemQuantityInStockFromDto(ItemQuantityInStockDTO itemQuantityInStockDTO, @MappingTarget ItemQuantityInStockEntity itemQuantityInStockEntity);

}
