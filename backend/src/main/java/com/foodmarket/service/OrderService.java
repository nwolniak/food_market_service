package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.OrderRequestDto;
import com.foodmarket.model.dto.OrderResponseDto;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderItemEntity;
import com.foodmarket.model.mapping.OrderMapper;
import com.foodmarket.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderMapper mapper;


    public OrderResponseDto addOrder(OrderRequestDto orderRequestDTO) {
        OrderEntity orderEntity = new OrderEntity();
        Set<OrderItemEntity> orderItems = cartService.getCartEntity(orderRequestDTO.cartId()).getCartItems().stream().map(cartItem -> {
            ItemEntity itemEntity = cartItem.getItemEntity();
            int quantity = cartItem.getQuantity();
            return new OrderItemEntity(orderEntity, itemEntity, quantity);
        }).collect(toSet());
        orderEntity.setOrderItems(orderItems);
        OrderEntity saved = orderRepository.save(orderEntity);
        log.info("Saved order with id: {}", orderEntity.getId());
        return mapper.orderEntityToDto(saved);
    }

    public OrderResponseDto getOrder(long orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::orderEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with %s id not found in order repository", orderId)));
    }

    public OrderEntity getOrderEntity(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with %s id not found in order repository", orderId)));
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::orderEntityToDto)
                .toList();
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }
}
