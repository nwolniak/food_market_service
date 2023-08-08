package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.entity.CartEntity;
import com.foodmarket.model.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

//    @Mapping(target = "itemId", nullValueCheckStrategy = ALWAYS)
//    @Mapping(source = "cartItems", target = "cartItems", qualifiedByName = "cartItemsMapping")
//    CartEntity cartDtoToEntity(CartDto cartDTO);

    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "cartItems", target = "cartItems", qualifiedByName = "cartItemsMapping")
    CartDto cartEntityToDto(CartEntity cartEntity);

    @Named("cartItemsMapping")
    default List<CartDto.ItemQuantity> cartItemsMapping(Set<CartItemEntity> cartItems) {
        return cartItems
                .stream()
                .map(this::cartItemEntityToItemQuantity)
                .toList();
    }

    @Mapping(source = "itemEntity.id", target = "itemId")
    CartDto.ItemQuantity cartItemEntityToItemQuantity(CartItemEntity cartItemEntity);

//    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
//    void updateItemFromDto(ItemDto itemDTO, @MappingTarget ItemEntity itemEntity);

}
