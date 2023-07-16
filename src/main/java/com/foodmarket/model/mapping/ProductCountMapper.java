package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.entity.ProductCountEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductCountMapper {

    ProductCountMapper INSTANCE = Mappers.getMapper(ProductCountMapper.class);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "quantityInStock", target = "quantity")
    ProductCountDTO productCountEntityToProductCountDTO(ProductCountEntity productCountEntity);

    @Mapping(source = "quantity", target = "quantityInStock")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductCountFromDto(ProductCountDTO productCountDTO, @MappingTarget ProductCountEntity productCountEntity);

}
