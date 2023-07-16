package com.foodmarket.controller;

import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food-market")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/products/{id}")
    public ProductDTO getProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }

    @GetMapping(value = "/products")
    public List<ProductDTO> getProducts() {
        return productService.getAllProducts();
    }

    @PostMapping(value = "/products")
    public ProductDTO postProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

}
