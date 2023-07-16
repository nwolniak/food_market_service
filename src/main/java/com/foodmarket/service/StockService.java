package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import com.foodmarket.model.mapping.ProductCountMapper;
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
    private final ProductCountMapper mapper;

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
        return mapper.productCountEntityToProductCountDTO(saved);
    }

    public ProductCountDTO getProductCount(long id) {
        return stockRepository.findById(id)
                .map(mapper::productCountEntityToProductCountDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Product Count with %s id not found in products counts repository.", id)));
    }

    public List<ProductCountDTO> getProductCounts() {
        return stockRepository.findAll()
                .stream()
                .map(mapper::productCountEntityToProductCountDTO)
                .toList();
    }

    public void deleteProductCount(long id) {
        log.info("Deleting product count with {} id.", id);
        stockRepository.deleteById(id);
    }

    public ProductCountDTO putProductCount(ProductCountDTO productCountDTO) {
        ProductCountEntity productCountEntity = stockRepository.findById(productCountDTO.productId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Product Count with %s id not found in products counts repository.", productCountDTO.productId())));
        mapper.updateProductCountFromDto(productCountDTO, productCountEntity);
        ProductCountEntity saved = stockRepository.save(productCountEntity);
        return mapper.productCountEntityToProductCountDTO(saved);
    }
}
