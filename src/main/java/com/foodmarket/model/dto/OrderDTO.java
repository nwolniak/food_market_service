package com.foodmarket.model.dto;

import java.util.List;

public record OrderDTO(List<ItemQuantity> orderedItems) {
    public record ItemQuantity(long id, int quantity) {
    }

}
