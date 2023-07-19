package com.foodmarket.controller;

import com.foodmarket.model.dto.CartDTO;
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
    public CartDTO getCart(@PathVariable long id) {
        return cartService.getCart(id);
    }

    @GetMapping("carts")
    public List<CartDTO> getCarts() {
        return cartService.getCarts();
    }

    @PostMapping("carts")
    public CartDTO addCart(@RequestBody CartDTO cartDTO) {
        return cartService.addCart(cartDTO);
    }

}
