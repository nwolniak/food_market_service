package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "orderedProducts", target = "orderedProducts", qualifiedByName = "orderedProductsMapping")
    OrderDTO orderEntityToOrderDto(OrderEntity orderEntity);

    @Named("orderedProductsMapping")
    default List<ProductCountDTO> map(List<OrderProductEntity> orderedProducts) {
        return orderedProducts
                .stream()
                .map(this::orderProductEntityToProductCountDTO)
                .toList();
    }

    @Mapping(source = "productEntity.id", target = "productId")
    ProductCountDTO orderProductEntityToProductCountDTO(OrderProductEntity orderProductEntity);

}