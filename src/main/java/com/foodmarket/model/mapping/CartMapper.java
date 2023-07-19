package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.CartDTO;
import com.foodmarket.model.entity.CartEntity;
import com.foodmarket.model.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

//    @Mapping(target = "id", nullValueCheckStrategy = ALWAYS)
//    @Mapping(source = "cartItems", target = "cartItems", qualifiedByName = "cartItemsMapping")
//    CartEntity cartDtoToEntity(CartDTO cartDTO);

    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "cartItems", target = "cartItems", qualifiedByName = "cartItemsMapping")
    CartDTO cartEntityToDto(CartEntity cartEntity);

    @Named("cartItemsMapping")
    default List<CartDTO.ItemQuantity> cartItemsMapping(List<CartItemEntity> cartItems) {
        return cartItems
                .stream()
                .map(this::cartItemEntityToItemQuantity)
                .toList();
    }

    @Mapping(source = "cartEntity.id", target = "id")
    CartDTO.ItemQuantity cartItemEntityToItemQuantity(CartItemEntity cartItemEntity);

//    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
//    void updateItemFromDto(ItemDTO itemDTO, @MappingTarget ItemEntity itemEntity);

}
