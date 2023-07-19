package com.foodmarket.controller;

import com.foodmarket.model.dto.ItemQuantityInStockDto;
import com.foodmarket.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food-market")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("stock/{id}")
    public ItemQuantityInStockDto getItemQuantity(@PathVariable long id) {
        return stockService.getItemQuantity(id);
    }

    @GetMapping("stock")
    public List<ItemQuantityInStockDto> getItemQuantities() {
        return stockService.getItemQuantities();
    }

    @PostMapping("stock")
    public ItemQuantityInStockDto postItemQuantity(@RequestBody ItemQuantityInStockDto itemQuantity) {
        return stockService.setItemQuantity(itemQuantity);
    }

    @DeleteMapping("stock/{id}")
    public void deleteItemQuantity(@PathVariable long id) {
        stockService.deleteItemQuantity(id);
    }

    @PutMapping("stock")
    public ItemQuantityInStockDto putItemQuantity(@RequestBody ItemQuantityInStockDto itemQuantity) {
        return stockService.putItemQuantity(itemQuantity);
    }

}
