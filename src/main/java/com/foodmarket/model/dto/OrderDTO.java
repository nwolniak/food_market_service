package com.foodmarket.model.dto;

import java.util.Set;

public record OrderDTO(Set<ProductDTO> orderedProducts) {}
