package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.CartDTO;
import com.foodmarket.model.entity.CartEntity;
import com.foodmarket.model.entity.CartItemEntity;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.mapping.CartMapper;
import com.foodmarket.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final CartMapper mapper;

    public CartDTO addCart(CartDTO cartDTO) {
        CartEntity cartEntity = new CartEntity();
        List<CartItemEntity> cartItems = cartDTO.cartItems().stream().map(cartItem -> {
            ItemEntity itemEntity = itemService.getItemEntity(cartItem.id());
            return new CartItemEntity(cartEntity, itemEntity, cartItem.quantity());
        }).toList();
        cartEntity.setCartItems(cartItems);
        CartEntity saved = cartRepository.save(cartEntity);
        return mapper.cartEntityToDto(saved);
    }

    public CartDTO getCart(long id) {
        return cartRepository.findById(id)
                .map(mapper::cartEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with %s id not found in carts repository", id)));
    }

    public List<CartDTO> getCarts() {
        return cartRepository.findAll()
                .stream()
                .map(mapper::cartEntityToDto)
                .toList();
    }
}
