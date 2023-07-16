package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import com.foodmarket.model.mapping.ServiceMapper;
import com.foodmarket.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductService productService;
    private final ServiceMapper serviceMapper;

    public ProductCountDTO setProductCount(ProductCountDTO productCountDTO) {
        AtomicReference<ProductCountEntity> reference = new AtomicReference<>();
        stockRepository.findById(productCountDTO.productId()).ifPresentOrElse(productCountEntity -> {
            productCountEntity.setQuantityInStock(productCountDTO.quantity());
            reference.set(productCountEntity);
        }, () -> {
            ProductEntity productEntity = productService.getProductEntity(productCountDTO.productId());
            reference.set(new ProductCountEntity(productEntity, productCountDTO.quantity()));
        });
        ProductCountEntity saved = stockRepository.save(reference.get());
        return serviceMapper.productCountEntityToProductCountDTO(saved);
    }

    public ProductCountDTO getProductCount(long id) {
        return stockRepository.findById(id)
                .map(serviceMapper::productCountEntityToProductCountDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Product Count with %s id not found in products counts repository.", id)));
    }

    public List<ProductCountDTO> getProductCounts() {
        return stockRepository.findAll()
                .stream()
                .map(serviceMapper::productCountEntityToProductCountDTO)
                .toList();
    }

    public void updateProductCount(ProductCountDTO productCountDTO) {
        ProductCountEntity productCountEntity = stockRepository.findById(productCountDTO.productId()).orElseThrow();
        productCountEntity.setQuantityInStock(productCountEntity.getQuantityInStock());
        stockRepository.save(productCountEntity);
    }

    public void deleteProductCount(long id) {
        log.info("Deleting product count with {} id.", id);
        stockRepository.deleteById(id);
    }
}
