package com.foodmarket.controller;

import com.foodmarket.model.dto.PaymentDto;
import com.foodmarket.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food-market")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("payments/{paymentId}")
    public PaymentDto getPayment(@PathVariable long paymentId) {
        return paymentService.getPayment(paymentId);
    }

    @GetMapping("allPayments")
    public List<PaymentDto> getPayments() {
        return paymentService.getAll();
    }

    @GetMapping("payments")
    public List<PaymentDto> getUserPayments(@CurrentSecurityContext SecurityContext context) {
        return paymentService.getUserPayments(context);
    }

    @PostMapping("payments")
    public PaymentDto addPayment(@RequestBody PaymentDto paymentDto) {
        return paymentService.addPayment(paymentDto);
    }

}
