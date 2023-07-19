package com.foodmarket.controller;

import com.foodmarket.model.dto.ItemDTO;
import com.foodmarket.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food-market")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("items/{id}")
    public ItemDTO getItem(@PathVariable long id) {
        return itemService.getItem(id);
    }

    @GetMapping("items")
    public List<ItemDTO> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("items")
    public ItemDTO postItem(@RequestBody ItemDTO itemDTO) {
        return itemService.addItem(itemDTO);
    }

    @DeleteMapping("items/{id}")
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }

    @PutMapping("items")
    public ItemDTO putItem(@RequestBody ItemDTO itemDTO) {
        return itemService.putItem(itemDTO);
    }

}
