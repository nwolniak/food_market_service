package com.foodmarket.service;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.ProductDTO;
import com.foodmarket.model.entity.ProductEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class ProductServiceIT {

    @Autowired
    private ProductService productService;

    private final ProductDTO productDTO1 = new ProductDTO(1L, "Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductDTO productDTO2 = new ProductDTO(2L, "Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductDTO productDTO3 = new ProductDTO(3L, "Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");


    @Test
    public void addProductTest() {
        // when
        ProductDTO saved = productService.addProduct(productDTO1);
        // then
        assertNotNull(saved);
        assertEquals(productDTO1.name(), saved.name());
    }

    @Test
    public void getProductTest() {
        // given
        ProductEntity productEntity = productService.addProductReturnEntity(productDTO1);
        // when
        ProductDTO product = productService.getProduct(productEntity.getId());
        // then
        assertNotNull(product);
        assertEquals(productDTO1, product);
    }

    @Test
    public void addMultipleProductsTest() {
        // given
        List<ProductDTO> expectedProducts = List.of(productDTO1, productDTO2, productDTO3);
        // when
        productService.addProduct(productDTO1);
        productService.addProduct(productDTO2);
        productService.addProduct(productDTO3);
        List<ProductDTO> allProducts = productService.getAllProducts();
        // then
        assertNotNull(allProducts);
        assertFalse(allProducts.isEmpty());
        assertTrue(isEqualCollection(expectedProducts, allProducts));
    }

    @Test
    public void addProductMultipleTimesTest() {
        // given
        List<ProductDTO> expectedProducts = List.of(productDTO1);
        // when
        productService.addProduct(productDTO1);
        productService.addProduct(productDTO1);
        List<ProductDTO> allProducts = productService.getAllProducts();
        // then
        assertNotNull(allProducts);
        assertFalse(allProducts.isEmpty());
        assertTrue(isEqualCollection(expectedProducts, allProducts));
    }

    @Test
    public void getNotExistingProductShouldThrowException() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> productService.getProduct(123));
    }

    @Test
    public void getNotExistingProductEntityShouldThrowException() {
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> productService.getProductEntity(123));
    }

}
