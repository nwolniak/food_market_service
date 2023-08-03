package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.dto.CartDto.ItemQuantity;
import com.foodmarket.model.entity.CartEntity;
import com.foodmarket.model.entity.CartItemEntity;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.entity.UserEntity;
import com.foodmarket.model.mapping.CartMapper;
import com.foodmarket.repository.CartRepository;
import com.foodmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final CartMapper mapper;

    @Transactional
    public CartDto addCart(CartDto cartDTO, UserEntity userEntity) {
        UserEntity byUsername = userRepository.findByUsername(userEntity.getUsername()).orElseThrow();
        CartEntity cartEntity = new CartEntity();
        Set<CartItemEntity> cartItems = cartDTO.cartItems().stream().map(cartItem -> {
            ItemEntity itemEntity = itemService.getItemEntity(cartItem.id());
            return new CartItemEntity(cartEntity, itemEntity, cartItem.quantity());
        }).collect(toSet());
        cartEntity.setCartItems(cartItems);
        byUsername.setCartEntity(cartEntity);
        cartEntity.setUserEntity(byUsername);
        CartEntity saved = cartRepository.save(cartEntity);
        return mapper.cartEntityToDto(saved);
    }

    public CartDto getCart(long id) {
        return cartRepository.findById(id)
                .map(mapper::cartEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with %s id not found in carts repository", id)));
    }

    public CartEntity getCartEntity(long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with %s id not found in carts repository", id)));
    }

    public List<CartDto> getCarts() {
        return cartRepository.findAll()
                .stream()
                .map(mapper::cartEntityToDto)
                .toList();
    }

    public CartDto patchCart(long id, List<ItemQuantity> cartItems) {
        CartEntity cartEntity = cartRepository.findById(id)
                .orElseThrow();
        Set<CartItemEntity> cartItemsToInsert = cartItems.stream().map(cartItem -> {
            ItemEntity itemEntity = itemService.getItemEntity(cartItem.id());
            return new CartItemEntity(cartEntity, itemEntity, cartItem.quantity());
        }).collect(toSet());

        cartItemsToInsert
                .forEach(cartItem -> {
                    cartEntity.removeCartItem(cartItem);
                    cartEntity.addCartItem(cartItem);
                });

        CartEntity saved = cartRepository.save(cartEntity);
        return mapper.cartEntityToDto(saved);
    }

    public CartDto deleteItem(long cartId, long itemId) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow();
        cartEntity.getCartItems().stream()
                .filter(cartItemEntity -> cartItemEntity.getItemEntity().getId().equals(itemId))
                .findFirst()
                .ifPresent(cartItemEntity -> {
                    cartEntity.removeCartItem(cartItemEntity);
                    cartRepository.save(cartEntity);
                });
        return mapper.cartEntityToDto(cartEntity);
    }

    public void deleteCart(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow();
        userEntity.setCartEntity(null);
        userRepository.save(userEntity);
        cartRepository.deleteById(id);
    }
}
