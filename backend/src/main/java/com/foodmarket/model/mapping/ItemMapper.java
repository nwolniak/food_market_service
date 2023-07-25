package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.model.entity.ItemEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", nullValueCheckStrategy = ALWAYS)
    ItemEntity itemDtoToEntity(ItemDto itemDTO);

    ItemDto itemEntityToDto(ItemEntity itemEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateItemFromDto(ItemDto itemDTO, @MappingTarget ItemEntity itemEntity);

}
