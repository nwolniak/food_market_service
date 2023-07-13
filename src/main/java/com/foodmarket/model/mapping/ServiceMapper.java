package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderProductEntity;
import com.foodmarket.model.entity.ProductEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ProductEntity productDtoToProductEntity(ProductDTO productDTO);

    ProductDTO productEntityToProductDto(ProductEntity productEntity);

    @Mapping(source = "orderedProducts", target = "orderedProducts", qualifiedByName = "orderedProductsMapToSet")
    OrderEntity orderDtoToOrderEntity(OrderDTO orderDTO);

    @Mapping(source = "orderedProducts", target = "orderedProducts", qualifiedByName = "orderedProductsSetToMap")
    OrderDTO orderEntityToOrderDto(OrderEntity orderEntity);

    @Named("orderedProductsMapToSet")
    default Set<OrderProductEntity> map(Map<ProductDTO, Integer> orderedProducts) {
        OrderEntity orderEntity = new OrderEntity();
        return orderedProducts.entrySet().stream()
                .map(entry -> Map.entry(productDtoToProductEntity(entry.getKey()), entry.getValue()))
                .map(entry -> new OrderProductEntity(orderEntity, entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

    @Named("orderedProductsSetToMap")
    default Map<ProductDTO, Integer> map(Set<OrderProductEntity> orderedProducts) {
        return orderedProducts.stream()
                .collect(Collectors.toMap(
                        orderProductEntity -> productEntityToProductDto(orderProductEntity.getProductEntity()),
                        OrderProductEntity::getQuantity));
    }

}
