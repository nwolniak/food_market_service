package com.foodmarket.controller;

import com.foodmarket.model.dto.CartDto;
import com.foodmarket.model.dto.CartDto.ItemQuantity;
import com.foodmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
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
    public CartDto addCart(@RequestBody CartDto cartDTO) {
        return cartService.addCart(cartDTO);
    }

    @PatchMapping("carts/{id}")
    public CartDto patchCart(@PathVariable long id, @RequestBody List<ItemQuantity> cartItems) {
        return cartService.patchCart(id, cartItems);
    }

    @DeleteMapping("carts/{id}")
    public void deleteCart(@PathVariable long id) {
        cartService.deleteCart(id);
    }

}
