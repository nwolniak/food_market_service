package com.foodmarket.controller;

import com.foodmarket.model.dto.ItemDto;
import com.foodmarket.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food-market")
@CrossOrigin
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("items/{id}")
    public ItemDto getItem(@PathVariable long id) {
        return itemService.getItem(id);
    }

    @GetMapping("items")
    public List<ItemDto> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("items")
    public ItemDto postItem(@RequestBody ItemDto itemDTO) {
        return itemService.addItem(itemDTO);
    }

    @DeleteMapping("items/{id}")
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }

    @PutMapping("items/{id}")
    public ItemDto putItem(@PathVariable long id, @RequestBody ItemDto itemDTO) {
        return itemService.putItem(id, itemDTO);
    }

}
