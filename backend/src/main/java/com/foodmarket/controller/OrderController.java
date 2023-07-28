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

    @GetMapping("/orders/{id}")
    public OrderResponseDto getOrder(@PathVariable long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("orders")
    public List<OrderResponseDto> getOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/orders")
    public OrderResponseDto postOrder(@RequestBody OrderRequestDto orderRequestDTO) {
        return orderService.addOrder(orderRequestDTO);
    }

    @DeleteMapping("orders/{id}")
    public void deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
    }

}
