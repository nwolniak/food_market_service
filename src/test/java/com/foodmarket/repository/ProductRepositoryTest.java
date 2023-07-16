package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private final ProductEntity productEntity1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductEntity productEntity2 = new ProductEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductEntity productEntity3 = new ProductEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final ProductEntity productEntityEqual1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductEntity productEntityEqual2 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");

    @Test
    public void saveTest() {
        // when
        ProductEntity saved = productRepository.save(productEntity1);
        // then
        assertNotNull(saved);
        assertEquals(productEntity1, saved);
    }

    @Test
    public void saveMultipleProductsTest() {
        // given
        List<ProductEntity> expectedProducts = List.of(productEntity1, productEntity2, productEntity3);
        // when
        productRepository.save(productEntity1);
        productRepository.save(productEntity2);
        productRepository.save(productEntity3);
        List<ProductEntity> allProducts = productRepository.findAll();
        // then
        assertNotNull(allProducts);
        assertFalse(allProducts.isEmpty());
        assertTrue(isEqualCollection(expectedProducts, allProducts));
    }

    @Test
    public void saveProductMultipleTimesTest() {
        // given
        List<ProductEntity> expectedProducts = List.of(productEntity1);
        // when
        productRepository.save(productEntity1);
        productRepository.save(productEntity1);
        List<ProductEntity> allProducts = productRepository.findAll();
        // then
        assertNotNull(allProducts);
        assertFalse(allProducts.isEmpty());
        assertTrue(isEqualCollection(expectedProducts, allProducts));
    }

    @Test
    public void saveEqualProductsTest() {
        // given
        List<ProductEntity> expectedProducts = List.of(productEntityEqual1);
        // when
        productRepository.save(productEntityEqual1);
        productRepository.save(productEntityEqual2);
        List<ProductEntity> allProducts = productRepository.findAll();
        // then
        assertNotNull(allProducts);
        assertFalse(allProducts.isEmpty());
        assertTrue(isEqualCollection(expectedProducts, allProducts));
    }

    @Test
    public void findByNameTest() {
        // when
        productRepository.save(productEntity1);
        Optional<ProductEntity> fromRepositoryOptional = productRepository.findByName(productEntity1.getName());
        // then
        assertTrue(fromRepositoryOptional.isPresent());
        ProductEntity fromRepository = fromRepositoryOptional.get();
        assertEquals(productEntity1, fromRepository);
    }

    @Test
    public void findByIdTest() {
        // when
        productRepository.save(productEntity1);
        Optional<ProductEntity> fromRepositoryOptional = productRepository.findById(productEntity1.getId());
        // then
        assertTrue(fromRepositoryOptional.isPresent());
        ProductEntity fromRepository = fromRepositoryOptional.get();
        assertEquals(productEntity1, fromRepository);
    }

}
