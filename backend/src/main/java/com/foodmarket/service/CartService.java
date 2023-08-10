package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.dto.CartDto.ItemQuantity;
import com.foodmarket.model.entity.CartEntity;
import com.foodmarket.model.entity.CartItemEntity;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.mapping.CartMapper;
import com.foodmarket.repository.CartRepository;
import com.foodmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public CartDto addCart(CartDto cartDTO) {
//        UserEntity byUsername = userRepository.findByUsername(userEntity.getUsername()).orElseThrow();
        CartEntity cartEntity = new CartEntity();
        Set<CartItemEntity> cartItems = cartDTO.cartItems().stream().map(cartItem -> {
            ItemEntity itemEntity = itemService.getItemEntity(cartItem.itemId());
            return new CartItemEntity(cartEntity, itemEntity, cartItem.quantity());
        }).collect(toSet());
        cartEntity.setCartItems(cartItems);
//        byUsername.setCartEntity(cartEntity);
//        cartEntity.setUserEntity(byUsername);
        CartEntity saved = cartRepository.save(cartEntity);
        return mapper.cartEntityToDto(saved);
    }

    public CartDto getCart(long cartId) {
        return cartRepository.findById(cartId)
                .map(mapper::cartEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with %s id not found in carts repository", cartId)));
    }

    public CartDto getCartByUserId(long userId) {
        return cartRepository.findByUserEntity_Id(userId)
                .map(mapper::cartEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with %s userId not found in carts repository", userId)));
    }

    public CartEntity getCartEntity(long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cart with %s id not found in carts repository", cartId)));
    }

    public List<CartDto> getCarts() {
        return cartRepository.findAll()
                .stream()
                .map(mapper::cartEntityToDto)
                .toList();
    }

    public CartDto patchCart(long cartId, List<ItemQuantity> cartItems) {
        CartEntity cartEntity = cartRepository.findById(cartId)
                .orElseThrow();
        Set<CartItemEntity> cartItemsToInsert = cartItems.stream().map(cartItem -> {
            ItemEntity itemEntity = itemService.getItemEntity(cartItem.itemId());
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

    public void deleteCart(long cartId) {
        Optional<CartEntity> cartEntityOptional = cartRepository.findById(cartId);
        if (cartEntityOptional.isEmpty()) {
            return;
        }
        CartEntity cartEntity = cartEntityOptional.get();
        userRepository.findById(cartEntity.getUserEntity().getId())
                .ifPresent(userEntity -> {
                    userEntity.setCartEntity(null);
                    userRepository.save(userEntity);
                });
        cartRepository.deleteById(cartId);
    }
}
