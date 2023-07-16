package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    ProductEntity productDtoToProductEntity(ProductDTO productDTO);

    ProductDTO productEntityToProductDto(ProductEntity productEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductDTO productDTO, @MappingTarget ProductEntity productEntity);

}
