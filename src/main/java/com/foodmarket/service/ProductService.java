package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductEntity;
import com.foodmarket.model.mapping.ServiceMapper;
import com.foodmarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ServiceMapper serviceMapper;

    public ProductDTO addProduct(ProductDTO productDTO) {
        ProductEntity productEntity = serviceMapper.productDtoToProductEntity(productDTO);
        ProductEntity saved = productRepository.save(productEntity);
        log.info("Saved product with {} id", saved.getId());
        return serviceMapper.productEntityToProductDto(saved);
    }

    public ProductEntity addProductReturnEntity(ProductDTO productDTO) {
        ProductEntity productEntity = serviceMapper.productDtoToProductEntity(productDTO);
        return productRepository.save(productEntity);
    }

    public ProductDTO getProduct(long id) {
        return productRepository.findById(id)
                .map(serviceMapper::productEntityToProductDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s id not found in products repository.", id)));
    }

    public ProductEntity getProductEntity(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Product with %s id not found in products repository.", id)));
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(serviceMapper::productEntityToProductDto)
                .toList();
    }

    public void deleteProduct(long id) {
        log.info("Deleting product with {} id.", id);
        productRepository.deleteById(id);
    }
}
