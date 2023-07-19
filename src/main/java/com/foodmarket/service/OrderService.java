package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.exceptions.OrderValidationException;
import com.foodmarket.exceptions.StockQuantityNotSatisfiedException;
import com.foodmarket.model.dto.ItemQuantityInStockDTO;
import com.foodmarket.model.dto.OrderDTO;
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
    private final ItemService itemService;
    private final StockService stockService;
    private final OrderMapper mapper;


    public OrderDTO addOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        List<OrderItemEntity> orderedItems = orderDTO.orderedItems().stream().map(orderedItem -> {
            ItemEntity itemEntity = itemService.getItemEntity(orderedItem.id());
            return new OrderItemEntity(orderEntity, itemEntity, orderedItem.quantity());
        }).toList();
        orderEntity.setOrderedItems(orderedItems);
        validate(orderEntity);
        OrderEntity saved = orderRepository.save(orderEntity);
        log.info("Saved order with id: {}", orderEntity.getId());
        saved.getOrderedItems().forEach(orderItemEntity -> {
            long itemId = orderItemEntity.getItemEntity().getId();
            int orderedQuantity = orderItemEntity.getQuantity();
            int currentQuantity = stockService.getItemQuantity(itemId).quantityInStock();
            stockService.setItemQuantity(new ItemQuantityInStockDTO(itemId, currentQuantity - orderedQuantity));
        });
        return mapper.orderEntityToDto(saved);
    }

    public OrderDTO getOrder(long id) {
        return orderRepository.findById(id)
                .map(mapper::orderEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Order with %s id not found in order repository", id)));
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(mapper::orderEntityToDto).toList();
    }

    private void validate(OrderEntity orderEntity) {
        List<OrderItemEntity> orderedProducts = orderEntity.getOrderedItems();
        if (orderedProducts.isEmpty()) {
            throw new OrderValidationException("Order cannot be empty");
        }
        orderedProducts.forEach(this::validate);
    }

    private void validate(OrderItemEntity orderItemEntity) {
        ItemQuantityInStockDTO itemQuantity = stockService.getItemQuantity(orderItemEntity.getItemEntity().getId());
        int currentQuantity = itemQuantity.quantityInStock();
        int orderedQuantity = orderItemEntity.getQuantity();
        if (currentQuantity - orderedQuantity < 0) {
            throw new StockQuantityNotSatisfiedException(String.format(
                    "Required %s from %s units for product %s",
                    orderItemEntity.getQuantity(),
                    itemQuantity.quantityInStock(),
                    orderItemEntity.getItemEntity().getName()));
        }
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }
}
