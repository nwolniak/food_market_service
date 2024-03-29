package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ItemQuantityInStockDto;
import com.foodmarket.model.entity.ItemQuantityInStockEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper
public interface ItemQuantityInStockMapper {

    ItemQuantityInStockMapper INSTANCE = Mappers.getMapper(ItemQuantityInStockMapper.class);

    ItemQuantityInStockDto itemQuantityInStockToDto(ItemQuantityInStockEntity itemQuantityInStockEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateItemQuantityInStockFromDto(ItemQuantityInStockDto itemQuantityInStockDTO, @MappingTarget ItemQuantityInStockEntity itemQuantityInStockEntity);

}
