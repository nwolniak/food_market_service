package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ItemDTO;
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

    public ItemDTO addItem(ItemDTO itemDTO) {
        ItemEntity itemEntity = mapper.itemDtoToEntity(itemDTO);
        ItemEntity saved = itemRepository.save(itemEntity);
        log.info("Saved product with {} id", saved.getId());
        return mapper.itemEntityToDto(saved);
    }

    public ItemEntity addItemReturnEntity(ItemDTO itemDTO) {
        ItemEntity itemEntity = mapper.itemDtoToEntity(itemDTO);
        return itemRepository.save(itemEntity);
    }

    public ItemDTO getItem(long id) {
        return itemRepository.findById(id)
                .map(mapper::itemEntityToDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item with %s id not found in items repository.", id)));
    }

    public ItemEntity getItemEntity(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item with %s id not found in items repository.", id)));
    }

    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(mapper::itemEntityToDto)
                .toList();
    }

    public void deleteItem(long id) {
        log.info("Deleting item with {} id.", id);
        itemRepository.deleteById(id);
    }

    public ItemDTO putItem(ItemDTO itemDTO) {
        ItemEntity itemEntity = itemRepository.findById(itemDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item with %s id not found in items repository.", itemDTO.id())));
        mapper.updateItemFromDto(itemDTO, itemEntity);
        ItemEntity saved = itemRepository.save(itemEntity);
        return mapper.itemEntityToDto(saved);
    }
}
