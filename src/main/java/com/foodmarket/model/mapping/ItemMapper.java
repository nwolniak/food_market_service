package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ItemDTO;
import com.foodmarket.model.entity.ItemEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", nullValueCheckStrategy = ALWAYS)
    ItemEntity itemDtoToEntity(ItemDTO itemDTO);

    ItemDTO itemEntityToDto(ItemEntity itemEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateItemFromDto(ItemDTO itemDTO, @MappingTarget ItemEntity itemEntity);

}
