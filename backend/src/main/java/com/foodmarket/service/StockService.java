package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemQuantityInStockDto;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.entity.ItemQuantityInStockEntity;
import com.foodmarket.model.mapping.ItemQuantityInStockMapper;
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
    private final ItemService itemService;
    private final ItemQuantityInStockMapper mapper;

    public ItemQuantityInStockDto setItemQuantity(ItemQuantityInStockDto itemQuantity) {
        AtomicReference<ItemQuantityInStockEntity> reference = new AtomicReference<>();
        stockRepository.findById(itemQuantity.id()).ifPresentOrElse(productCountEntity -> {
            productCountEntity.setQuantityInStock(itemQuantity.quantityInStock());
            reference.set(productCountEntity);
        }, () -> {
            ItemEntity itemEntity = itemService.getItemEntity(itemQuantity.id());
            reference.set(new ItemQuantityInStockEntity(itemEntity, itemQuantity.quantityInStock()));
        });
        ItemQuantityInStockEntity saved = stockRepository.save(reference.get());
        return mapper.itemQuantityInStockToDto(saved);
    }

    public ItemQuantityInStockDto getItemQuantity(long id) {
        return stockRepository.findById(id)
                .map(mapper::itemQuantityInStockToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Item quantity with %s itemId not found in stock repository.", id)));
    }

    public List<ItemQuantityInStockDto> getItemQuantities() {
        return stockRepository.findAll()
                .stream()
                .map(mapper::itemQuantityInStockToDto)
                .toList();
    }

    public void deleteItemQuantity(long id) {
        log.info("Deleting item quantity with {} itemId.", id);
        stockRepository.deleteById(id);
    }

    public ItemQuantityInStockDto putItemQuantity(ItemQuantityInStockDto itemQuantity) {
        ItemQuantityInStockEntity itemQuantityInStockEntity = stockRepository.findById(itemQuantity.id())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Item quantity with %s itemId not found in stock repository.", itemQuantity.id())));
        mapper.updateItemQuantityInStockFromDto(itemQuantity, itemQuantityInStockEntity);
        ItemQuantityInStockEntity saved = stockRepository.save(itemQuantityInStockEntity);
        return mapper.itemQuantityInStockToDto(saved);
    }
}
