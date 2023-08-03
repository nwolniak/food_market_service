package com.foodmarket.controller;

import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.dto.CartDto.ItemQuantity;
import com.foodmarket.model.entity.UserEntity;
import com.foodmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
@CrossOrigin
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("carts/{id}")
    public CartDto getCart(@PathVariable long id) {
        return cartService.getCart(id);
    }

    @GetMapping("carts")
    public List<CartDto> getCarts() {
        return cartService.getCarts();
    }

    @PostMapping("carts")
    public CartDto addCart(@RequestBody CartDto cartDTO, @AuthenticationPrincipal UserEntity userEntity) {
        return cartService.addCart(cartDTO, userEntity);
    }

    @PatchMapping("carts/{cartId}")
    public CartDto patchCart(@PathVariable long cartId, @RequestBody List<ItemQuantity> cartItems) {
        return cartService.patchCart(cartId, cartItems);
    }

    @DeleteMapping("carts/{cartId}/{itemId}")
    public CartDto deleteItem(@PathVariable long cartId, @PathVariable long itemId) {
        return cartService.deleteItem(cartId, itemId);
    }

    @DeleteMapping("carts/{cartId}")
    public void deleteCart(@PathVariable long cartId) {
        cartService.deleteCart(cartId);
    }

}
