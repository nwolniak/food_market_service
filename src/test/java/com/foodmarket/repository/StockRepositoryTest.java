package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.ProductCountEntity;
import com.foodmarket.model.entity.ProductEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TestConfiguration.class)
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    private final ProductEntity productEntity1 = new ProductEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private final ProductEntity productEntity2 = new ProductEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private final ProductEntity productEntity3 = new ProductEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private final ProductCountEntity product1CountEntity = new ProductCountEntity(productEntity1, 50);
    private final ProductCountEntity product2CountEntity = new ProductCountEntity(productEntity2, 40);
    private final ProductCountEntity product3CountEntity = new ProductCountEntity(productEntity3, 35);

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
        // when
        stockRepository.save(product1CountEntity);
        stockRepository.save(product2CountEntity);
        stockRepository.save(product3CountEntity);
        List<ProductCountEntity> allProductCountEntityList = stockRepository.findAll();
        // then
        List<ProductCountEntity> expectedProductCountEntityList = List.of(product1CountEntity, product2CountEntity, product3CountEntity);
        assertFalse(allProductCountEntityList.isEmpty());
        assertTrue(CollectionUtils.isEqualCollection(expectedProductCountEntityList, allProductCountEntityList));
    }

    @Test
    public void saveProductCountMultipleTimesTest() {
        // when
        stockRepository.save(product1CountEntity);
        stockRepository.save(product1CountEntity);
        List<ProductCountEntity> allProductCountEntityList = stockRepository.findAll();
        // then
        List<ProductCountEntity> expectedProductCountEntityList = List.of(product1CountEntity);
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
