package com.foodmarket.repository;

import com.foodmarket.configuration.TestConfiguration;
import com.foodmarket.model.entity.ItemQuantityInStockEntity;
import com.foodmarket.model.entity.ItemEntity;
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
    private ItemRepository itemRepository;

    private ItemEntity itemEntity1 = new ItemEntity("Bananas", "Fruit", "Bunch", 2.99, "Fresh, ripe bananas");
    private ItemEntity itemEntity2 = new ItemEntity("Apples", "Fruit", "Bag", 4.99, "Juicy, crunchy apples");
    private ItemEntity itemEntity3 = new ItemEntity("Oranges", "Fruit", "Bag", 3.99, "Sweet and tangy oranges");

    private ItemQuantityInStockEntity product1CountEntity;
    private ItemQuantityInStockEntity product2CountEntity;
    private ItemQuantityInStockEntity product3CountEntity;

    @BeforeEach
    public void init() {
        itemEntity1 = itemRepository.save(itemEntity1);
        itemEntity2 = itemRepository.save(itemEntity2);
        itemEntity3 = itemRepository.save(itemEntity3);
        product1CountEntity = new ItemQuantityInStockEntity(itemEntity1, 50);
        product2CountEntity = new ItemQuantityInStockEntity(itemEntity2, 40);
        product3CountEntity = new ItemQuantityInStockEntity(itemEntity3, 35);
    }

    @Test
    public void saveTest() {
        // when
        ItemQuantityInStockEntity saved = stockRepository.save(product1CountEntity);
        // then
        assertNotNull(saved);
        assertEquals(product1CountEntity, saved);
    }

    @Test
    public void saveMultipleProductCountsTest() {
        // given
        List<ItemQuantityInStockEntity> expectedItemQuantityInStockEntityList = List.of(product1CountEntity, product2CountEntity, product3CountEntity);
        // when
        stockRepository.save(product1CountEntity);
        stockRepository.save(product2CountEntity);
        stockRepository.save(product3CountEntity);
        List<ItemQuantityInStockEntity> allItemQuantityInStockEntityList = stockRepository.findAll();
        // then
        assertNotNull(allItemQuantityInStockEntityList);
        assertFalse(allItemQuantityInStockEntityList.isEmpty());
        assertTrue(isEqualCollection(expectedItemQuantityInStockEntityList, allItemQuantityInStockEntityList));
    }

    @Test
    public void saveProductCountMultipleTimesTest() {
        // given
        List<ItemQuantityInStockEntity> expectedItemQuantityInStockEntityList = List.of(product1CountEntity);
        // when
        stockRepository.save(product1CountEntity);
        stockRepository.save(product1CountEntity);
        List<ItemQuantityInStockEntity> allItemQuantityInStockEntityList = stockRepository.findAll();
        // then
        assertNotNull(allItemQuantityInStockEntityList);
        assertFalse(allItemQuantityInStockEntityList.isEmpty());
        assertEquals(expectedItemQuantityInStockEntityList, allItemQuantityInStockEntityList);
    }

    @Test
    public void findByIdTest() {
        // when
        stockRepository.save(product1CountEntity);
        Optional<ItemQuantityInStockEntity> fromDatabaseOptional = stockRepository.findById(product1CountEntity.getId());
        // then
        assertTrue(fromDatabaseOptional.isPresent());
        ItemQuantityInStockEntity fromDatabase = fromDatabaseOptional.get();
        assertEquals(product1CountEntity, fromDatabase);
    }

    @Test
    public void incrementCounterTest() {
        // given
        ItemQuantityInStockEntity saved = stockRepository.save(product1CountEntity);
        // when
        saved.setQuantityInStock(saved.getQuantityInStock() + 1);
        stockRepository.save(saved);
        // then
        Optional<ItemQuantityInStockEntity> incremented = stockRepository.findById(saved.getId());
        assertEquals(51, incremented.get().getQuantityInStock());
    }

    @Test
    public void decrementCounterTest() {
        // given
        ItemQuantityInStockEntity saved = stockRepository.save(product1CountEntity);
        // when
        saved.setQuantityInStock(saved.getQuantityInStock() - 1);
        stockRepository.save(saved);
        // then
        Optional<ItemQuantityInStockEntity> decremented = stockRepository.findById(saved.getId());
        assertEquals(49, decremented.get().getQuantityInStock());
    }

    @Test
    public void deleteByIdTest() {
        // given
        ItemQuantityInStockEntity saved = stockRepository.save(product1CountEntity);
        // when
        stockRepository.deleteById(saved.getId());
        Optional<ItemQuantityInStockEntity> deleted = stockRepository.findById(saved.getId());
        // then
        assertTrue(deleted.isEmpty());
    }

}
