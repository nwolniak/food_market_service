package com.foodmarket.model.dto;

import java.util.List;

public record OrderResponseDto(long orderId, List<ItemQuantity> orderedItems) {
    public record ItemQuantity(long itemId, int quantity) {
    }

}
