package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.exceptions.OrderValidationException;
import com.foodmarket.exceptions.StockQuantityNotSatisfiedException;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderProductEntity;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import com.foodmarket.model.mapping.ServiceMapper;
import com.foodmarket.repository.OrderRepository;
import com.foodmarket.repository.ProductRepository;
import com.foodmarket.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final ServiceMapper serviceMapper;

    public OrderDTO addOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = serviceMapper.orderDtoToOrderEntity(orderDTO);
        validate(orderEntity);
        orderRepository.save(orderEntity);
        orderEntity.getOrderedProducts()
                .forEach(orderProductEntity -> {
                    stockRepository.findById(orderProductEntity.getProductEntity().getId())
                            .ifPresent(productCountEntity -> {
                                productCountEntity.setQuantityInStock(productCountEntity.getQuantityInStock() - orderProductEntity.getQuantity());
                                stockRepository.save(productCountEntity);
                            });
                });
        return serviceMapper.orderEntityToOrderDto(orderEntity);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(serviceMapper::orderEntityToOrderDto)
                .toList();
    }

    private void validate(OrderEntity orderEntity) {
        Set<OrderProductEntity> orderedProducts = orderEntity.getOrderedProducts();
        if (orderedProducts.isEmpty()) {
            throw new OrderValidationException("Order cannot be empty");
        }
        orderedProducts.forEach(this::validate);
    }

    private void validate(OrderProductEntity orderProductEntity) {
        String productName = Optional.ofNullable(orderProductEntity.getProductEntity())
                .map(ProductEntity::getName)
                .orElseThrow();
        ProductEntity productEntity = productRepository.findByName(productName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s name not found", productName)));
        ProductCountEntity productCountEntity = stockRepository.findById(productEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s id not found", productEntity.getId())));
        if (productCountEntity.getQuantityInStock() - orderProductEntity.getQuantity() < 0) {
            throw new StockQuantityNotSatisfiedException(String.format("Required %s from %s for product %s",
                    orderProductEntity.getQuantity(), productCountEntity.getQuantityInStock(), productEntity));
        }
    }

}
