package com.foodmarket.controller;

import com.foodmarket.model.dto.ItemQuantityInStockDTO;
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
    public ItemQuantityInStockDTO getItemQuantity(@PathVariable long id) {
        return stockService.getItemQuantity(id);
    }

    @GetMapping("stock")
    public List<ItemQuantityInStockDTO> getItemQuantities() {
        return stockService.getItemQuantities();
    }

    @PostMapping("stock")
    public ItemQuantityInStockDTO postItemQuantity(@RequestBody ItemQuantityInStockDTO itemQuantity) {
        return stockService.setItemQuantity(itemQuantity);
    }

    @DeleteMapping("stock/{id}")
    public void deleteItemQuantity(@PathVariable long id) {
        stockService.deleteItemQuantity(id);
    }

    @PutMapping("stock")
    public ItemQuantityInStockDTO putItemQuantity(@RequestBody ItemQuantityInStockDTO itemQuantity) {
        return stockService.putItemQuantity(itemQuantity);
    }

}
