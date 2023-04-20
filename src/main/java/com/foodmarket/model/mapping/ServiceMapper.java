package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ProductEntity productDtoToProductEntity(ProductDTO productDTO);

    ProductDTO productEntityToProductDto(ProductEntity productEntity);

    @Mapping(source = "orderedProducts", target = "orderedProducts")
    OrderEntity orderDtoToOrderEntity(OrderDTO orderDTO);

    @Mapping(source = "orderedProducts", target = "orderedProducts")
    OrderDTO orderEntityToOrderDto(OrderEntity orderEntity);

}
