package com.foodmarket.controller;

import com.foodmarket.model.dto.ProductCountDTO;
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
    public ProductCountDTO getProductCount(@PathVariable long id) {
        return stockService.getProductCount(id);
    }

    @GetMapping("stock")
    public List<ProductCountDTO> getProductCounts() {
        return stockService.getProductCounts();
    }

    @PostMapping("stock")
    public ProductCountDTO postProductCount(@RequestBody ProductCountDTO productCountDTO) {
        return stockService.setProductCount(productCountDTO);
    }

    @DeleteMapping("stock/{id}")
    public void deleteProductCount(@PathVariable long id) {
        stockService.deleteProductCount(id);
    }

}
