package com.foodmarket.controller;

import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/{id}")
    public OrderDTO getOrder(@PathVariable long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("orders")
    public List<OrderDTO> getOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping("/orders")
    public OrderDTO postOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.addOrder(orderDTO);
    }

    @DeleteMapping("orders/{id}")
    public void deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
    }

}
