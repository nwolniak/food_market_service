package com.foodmarket.model.dto;


public record ItemDto(Long id, String name, String category, String unitType, double unitPrice, String description) {}
