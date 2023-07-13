package com.foodmarket.model.dto;

import java.util.Map;

public record OrderDTO(Map<ProductDTO, Integer> orderedProducts) {}
