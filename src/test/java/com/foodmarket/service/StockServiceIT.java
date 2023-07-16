package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ProductCountDTO;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class StockServiceIT {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    private final ProductDTO productDTO1 = new ProductDTO("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @Test
    public void setProductCountTest() {
        // given
        ProductEntity productEntity = productService.addProductReturnEntity(productDTO1);
        ProductCountDTO productCountDTO = new ProductCountDTO(productEntity.getId(), 5);
        // when
        ProductCountDTO saved = stockService.setProductCount(productCountDTO);
        // then
        assertNotNull(saved);
        assertEquals(productCountDTO, saved);
    }

    @Test
    public void getProductCountTest() {
        // given
        ProductEntity productEntity = productService.addProductReturnEntity(productDTO1);
        ProductCountDTO productCountDTO = new ProductCountDTO(productEntity.getId(), 5);
        stockService.setProductCount(productCountDTO);
        // when
        ProductCountDTO saved = stockService.getProductCount(productCountDTO.productId());
        // then
        assertNotNull(saved);
        assertEquals(productCountDTO, saved);
    }

    @Test
    public void getProductCountsTest() {
        // given
        ProductEntity productEntity1 = productService.addProductReturnEntity(productDTO1);
        ProductEntity productEntity2 = productService.addProductReturnEntity(productDTO2);
        ProductEntity productEntity3 = productService.addProductReturnEntity(productDTO3);
        ProductCountDTO productCountDTO1 = new ProductCountDTO(productEntity1.getId(), 5);
        ProductCountDTO productCountDTO2 = new ProductCountDTO(productEntity2.getId(), 10);
        ProductCountDTO productCountDTO3 = new ProductCountDTO(productEntity3.getId(), 15);
        stockService.setProductCount(productCountDTO1);
        stockService.setProductCount(productCountDTO2);
        stockService.setProductCount(productCountDTO3);
        List<ProductCountDTO> expectedProductCounts = List.of(productCountDTO1, productCountDTO2, productCountDTO3);
        // when
        List<ProductCountDTO> allProductCounts = stockService.getProductCounts();
        // then
        assertNotNull(allProductCounts);
        assertFalse(allProductCounts.isEmpty());
        assertTrue(CollectionUtils.isEqualCollection(expectedProductCounts, allProductCounts));
    }

    @Test
    public void getNotExistingProductCountShouldThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> stockService.getProductCount(123));
    }

}
