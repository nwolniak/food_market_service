package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class MarketServiceIT {

    @Autowired
    private MarketService marketService;

    private final ProductDTO productDTO1 = new ProductDTO("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    @Test
    public void addProductTest() {
        // when
        ProductDTO saved = marketService.addProduct(productDTO1);
        // then
        assertNotNull(saved);
        assertEquals(productDTO1, saved);
        assertEquals(1, marketService.getQuantityInStock(productDTO1));
    }

    @Test
    public void getProductByNameTest() {
        // when
        marketService.addProduct(productDTO1);
        ProductDTO fromService = marketService.getProductByName(productDTO1.name());
        // then
        assertNotNull(fromService);
        assertEquals(productDTO1, fromService);
    }

    @Test
    public void getProductByNameNotFoundException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            marketService.getProductByName("random name");
        });
    }

    @Test
    public void getProductByIdNotFoundException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            marketService.getProductById(new Random().nextLong());
        });
    }

    @Test
    public void getAllProductsTest() {
        // when
        marketService.addProduct(productDTO1);
        marketService.addProduct(productDTO2);
        marketService.addProduct(productDTO3);
        List<ProductDTO> allProducts = marketService.getAllProducts();
        // then
        List<ProductDTO> expectedProductDtoList = List.of(productDTO1, productDTO2, productDTO3);
        assertFalse(allProducts.isEmpty());
        assertEquals(expectedProductDtoList, allProducts);
    }

}
