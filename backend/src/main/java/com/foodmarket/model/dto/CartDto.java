package com.foodmarket.model.dto;

import java.util.List;

public record CartDto(Long cartId, List<ItemQuantity> cartItems) {

    public record ItemQuantity(long itemId, int quantity) {
    }

}
