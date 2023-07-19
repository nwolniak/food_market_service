package com.foodmarket.model.dto;

import java.util.List;

public record CartDTO(Long cartId, List<ItemQuantity> cartItems) {

    public record ItemQuantity(long id, int quantity) {
    }

}
