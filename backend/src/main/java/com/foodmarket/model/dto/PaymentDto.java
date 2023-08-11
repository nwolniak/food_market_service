package com.foodmarket.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record PaymentDto(Long paymentId, double amount, LocalDateTime paymentDate,
                         Long orderId) implements Serializable {
}