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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderMapper mapper;


    public OrderResponseDto addOrder(OrderRequestDto orderRequestDTO) {
        OrderEntity orderEntity = new OrderEntity();
        List<OrderItemEntity> orderedItems = cartService.getCartEntity(orderRequestDTO.cartId()).getCartItems().stream().map(cartItem -> {
            ItemEntity itemEntity = cartItem.getItemEntity();
            int quantity = cartItem.getQuantity();
            return new OrderItemEntity(orderEntity, itemEntity, quantity);
        }).toList();
        orderEntity.setOrderedItems(orderedItems);
        OrderEntity saved = orderRepository.save(orderEntity);
        log.info("Saved order with itemId: {}", orderEntity.getId());
        return mapper.orderEntityToDto(saved);
    }

    public OrderResponseDto getOrder(long id) {
        return orderRepository.findById(id)
                .map(mapper::orderEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with %s itemId not found in order repository", id)));
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
