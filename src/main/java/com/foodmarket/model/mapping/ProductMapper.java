package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductEntity productDtoToProductEntity(ProductDTO productDTO);

    ProductDTO productEntityToProductDto(ProductEntity productEntity);

}
