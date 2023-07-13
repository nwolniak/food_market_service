package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.OrderDTO;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import com.foodmarket.model.mapping.ServiceMapper;
import com.foodmarket.repository.ProductRepository;
import com.foodmarket.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MarketService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final OrderService orderService;
    private final ServiceMapper serviceMapper;

    public OrderDTO addOrder(OrderDTO orderDTO) {
        return orderService.addOrder(orderDTO);
    }

    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO) {
        ProductEntity productEntity = productRepository.findByName(productDTO.name())
                .orElseGet(() -> {
                    ProductEntity product = serviceMapper.productDtoToProductEntity(productDTO);
                    return productRepository.save(product);
                });

        stockRepository.findById(productEntity.getId())
                .ifPresentOrElse(
                        productCountEntity -> {
                            productCountEntity.setQuantityInStock(productCountEntity.getQuantityInStock() + 1);
                            stockRepository.save(productCountEntity);
                        },
                        () -> stockRepository.save(new ProductCountEntity(productEntity, 1))
                );

        return serviceMapper.productEntityToProductDto(productEntity);
    }

    public ProductDTO getProductByName(String name) {
        return productRepository.findByName(name)
                .map(serviceMapper::productEntityToProductDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s name not found", name)));
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(serviceMapper::productEntityToProductDto)
                .toList();
    }

    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(serviceMapper::productEntityToProductDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s id not found", id)));
    }

    public int getQuantityInStock(ProductDTO productDTO) {
        Long productId = productRepository.findByName(productDTO.name())
                .map(ProductEntity::getId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s name not found", productDTO.name())));
        return stockRepository.findById(productId)
                .map(ProductCountEntity::getQuantityInStock)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s id not found", productId)));
    }

}
