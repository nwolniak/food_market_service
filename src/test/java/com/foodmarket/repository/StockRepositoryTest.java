package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
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
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    private ProductEntity productEntity1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private ProductEntity productEntity2 = new ProductEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private ProductEntity productEntity3 = new ProductEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private ProductCountEntity product1CountEntity;
    private ProductCountEntity product2CountEntity;
    private ProductCountEntity product3CountEntity;

    @BeforeEach
    public void init() {
        productEntity1 = productRepository.save(productEntity1);
        productEntity2 = productRepository.save(productEntity2);
        productEntity3 = productRepository.save(productEntity3);
        product1CountEntity = new ProductCountEntity(productEntity1, 50);
        product2CountEntity = new ProductCountEntity(productEntity2, 40);
        product3CountEntity = new ProductCountEntity(productEntity3, 35);
    }

    @Test
    public void saveTest() {
        // when
        ProductCountEntity saved = stockRepository.save(product1CountEntity);
        // then
        assertNotNull(saved);
        assertEquals(product1CountEntity, saved);
    }

    @Test
    public void saveMultipleProductCountsTest() {
        // given
        List<ProductCountEntity> expectedProductCountEntityList = List.of(product1CountEntity, product2CountEntity, product3CountEntity);
        // when
        stockRepository.save(product1CountEntity);
        stockRepository.save(product2CountEntity);
        stockRepository.save(product3CountEntity);
        List<ProductCountEntity> allProductCountEntityList = stockRepository.findAll();
        // then
        assertNotNull(allProductCountEntityList);
        assertFalse(allProductCountEntityList.isEmpty());
        assertTrue(isEqualCollection(expectedProductCountEntityList, allProductCountEntityList));
    }

    @Test
    public void saveProductCountMultipleTimesTest() {
        // given
        List<ProductCountEntity> expectedProductCountEntityList = List.of(product1CountEntity);
        // when
        stockRepository.save(product1CountEntity);
        stockRepository.save(product1CountEntity);
        List<ProductCountEntity> allProductCountEntityList = stockRepository.findAll();
        // then
        assertNotNull(allProductCountEntityList);
        assertFalse(allProductCountEntityList.isEmpty());
        assertEquals(expectedProductCountEntityList, allProductCountEntityList);
    }

    @Test
    public void findByIdTest() {
        // when
        stockRepository.save(product1CountEntity);
        Optional<ProductCountEntity> fromDatabaseOptional = stockRepository.findById(product1CountEntity.getId());
        // then
        assertTrue(fromDatabaseOptional.isPresent());
        ProductCountEntity fromDatabase = fromDatabaseOptional.get();
        assertEquals(product1CountEntity, fromDatabase);
    }

    @Test
    public void incrementCounterTest() {
        // given
        ProductCountEntity saved = stockRepository.save(product1CountEntity);
        // when
        saved.setQuantityInStock(saved.getQuantityInStock() + 1);
        stockRepository.save(saved);
        // then
        Optional<ProductCountEntity> incremented = stockRepository.findById(saved.getId());
        assertEquals(51, incremented.get().getQuantityInStock());
    }

    @Test
    public void decrementCounterTest() {
        // given
        ProductCountEntity saved = stockRepository.save(product1CountEntity);
        // when
        saved.setQuantityInStock(saved.getQuantityInStock() - 1);
        stockRepository.save(saved);
        // then
        Optional<ProductCountEntity> decremented = stockRepository.findById(saved.getId());
        assertEquals(49, decremented.get().getQuantityInStock());
    }

}
