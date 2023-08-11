package com.foodmarket.controller;

import com.foodmarket.model.dto.OrderRequestDto;
import com.foodmarket.model.dto.OrderResponseDto;
import com.foodmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
@CrossOrigin
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/{orderId}")
    public OrderResponseDto getOrder(@PathVariable long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("orders")
    public List<OrderResponseDto> getOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/orders")
    public OrderResponseDto postOrder(@RequestBody OrderRequestDto orderRequestDTO) {
        return orderService.addOrder(orderRequestDTO);
    }

    @DeleteMapping("orders/{orderId}")
    public void deleteOrder(@PathVariable long orderId) {
        orderService.deleteOrder(orderId);
    }

}
