package com.foodmarket.model.dto;

import java.time.LocalDateTime;

public record UserDto(long id, String name, String username, String email, LocalDateTime createdDate) {}
