package com.foodmarket.service;

import com.foodmarket.exceptions.EntityDeleteException;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.model.entity.ItemEntity;
import com.foodmarket.model.mapping.ItemMapper;
import com.foodmarket.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper mapper;

    public ItemDto addItem(ItemDto itemDTO) {
        ItemEntity itemEntity = mapper.itemDtoToEntity(itemDTO);
        ItemEntity saved = itemRepository.save(itemEntity);
        log.info("Saved product with {} itemId", saved.getId());
        return mapper.itemEntityToDto(saved);
    }

    public ItemEntity addItemReturnEntity(ItemDto itemDTO) {
        ItemEntity itemEntity = mapper.itemDtoToEntity(itemDTO);
        return itemRepository.save(itemEntity);
    }

    public ItemDto getItem(long id) {
        return itemRepository.findById(id).map(mapper::itemEntityToDto).orElseThrow(() -> new EntityNotFoundException(String.format("Item with %s itemId not found in items repository.", id)));
    }

    public ItemEntity getItemEntity(long id) {
        return itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Item with %s itemId not found in items repository.", id)));
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream().map(mapper::itemEntityToDto).toList();
    }

    public void deleteItem(long id) {
        itemRepository.findById(id).ifPresent(itemEntity -> {
            if (!itemEntity.getCarts().isEmpty()) {
                throw new EntityDeleteException(String.format("Item with %s itemId is contained in existing carts.", id));
            } else if (!itemEntity.getOrders().isEmpty()) {
                throw new EntityDeleteException(String.format("Item with %s itemId is contained in existing orders.", id));
            }
        });
        log.info("Deleting item with {} itemId.", id);
        itemRepository.deleteById(id);
    }

    public ItemDto putItem(long id, ItemDto itemDTO) {
        ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Item with %s itemId not found in items repository.", itemDTO.id())));
        mapper.updateItemFromDto(itemDTO, itemEntity);
        ItemEntity saved = itemRepository.save(itemEntity);
        return mapper.itemEntityToDto(saved);
    }
}
