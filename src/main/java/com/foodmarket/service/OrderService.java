package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.exceptions.OrderValidationException;
import com.foodmarket.exceptions.StockQuantityNotSatisfiedException;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.OrderProductEntity;
import com.foodmarket.model.entity.ProductEntity;
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
    private final ProductService productService;
    private final StockService stockService;
    private final OrderMapper mapper;


    public OrderDTO addOrder(OrderDTO orderDTO) {
        OrderEntity orderEntity = new OrderEntity();
        List<OrderProductEntity> orderedProducts = orderDTO.orderedProducts().stream().map(productCountDTO -> {
            ProductEntity productEntity = productService.getProductEntity(productCountDTO.productId());
            return new OrderProductEntity(orderEntity, productEntity, productCountDTO.quantity());
        }).toList();
        orderEntity.setOrderedProducts(orderedProducts);
        validate(orderEntity);
        OrderEntity saved = orderRepository.save(orderEntity);
        log.info("Saved order with id: {}", orderEntity.getId());
        saved.getOrderedProducts().forEach(orderProductEntity -> {
            long productId = orderProductEntity.getProductEntity().getId();
            int orderedQuantity = orderProductEntity.getQuantity();
            int currentQuantity = stockService.getProductCount(productId).quantity();
            stockService.setProductCount(new ProductCountDTO(productId, currentQuantity - orderedQuantity));
        });
        return mapper.orderEntityToOrderDto(saved);
    }

    public OrderDTO getOrder(long id) {
        return orderRepository.findById(id)
                .map(mapper::orderEntityToOrderDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Order with %s id not found in order repository", id)));
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(mapper::orderEntityToOrderDto).toList();
    }

    private void validate(OrderEntity orderEntity) {
        List<OrderProductEntity> orderedProducts = orderEntity.getOrderedProducts();
        if (orderedProducts.isEmpty()) {
            throw new OrderValidationException("Order cannot be empty");
        }
        orderedProducts.forEach(this::validate);
    }

    private void validate(OrderProductEntity orderProductEntity) {
        ProductCountDTO productCountDTO = stockService.getProductCount(orderProductEntity.getProductEntity().getId());
        int currentQuantity = productCountDTO.quantity();
        int orderedQuantity = orderProductEntity.getQuantity();
        if (currentQuantity - orderedQuantity < 0) {
            throw new StockQuantityNotSatisfiedException(String.format(
                    "Required %s from %s units for product %s",
                    orderProductEntity.getQuantity(),
                    productCountDTO.quantity(),
                    orderProductEntity.getProductEntity().getName()));
        }
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }
}
